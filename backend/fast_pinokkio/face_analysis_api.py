from fastapi import FastAPI, HTTPException
from fastapi.responses import JSONResponse
import numpy as np
import cv2
import base64
import json
import uuid
import logging
from typing import List
from deepface import DeepFace
from pydantic import BaseModel

# 로깅 설정
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

app = FastAPI()

class ImageData(BaseModel):
    images: List[str]

class AnalysisResult(BaseModel):
    age: int
    gender: str
    is_face: bool
    encrypted_embedding: str

def preprocess_face(frame):
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    clahe = cv2.createCLAHE(clipLimit=2.0, tileGridSize=(8,8))
    equalized = clahe.apply(gray)
    blurred = cv2.GaussianBlur(equalized, (5, 5), 0)
    processed = cv2.cvtColor(blurred, cv2.COLOR_GRAY2BGR)
    return processed

def augment_image(image):
    augmentations = []
    augmentations.append(image)
    augmentations.append(cv2.rotate(image, cv2.ROTATE_90_CLOCKWISE))
    augmentations.append(cv2.rotate(image, cv2.ROTATE_90_COUNTERCLOCKWISE))
    augmentations.append(cv2.convertScaleAbs(image, alpha=1.2, beta=30))
    augmentations.append(cv2.convertScaleAbs(image, alpha=0.8, beta=-30))
    return augmentations

@app.post("/analyze_faces")
async def analyze_faces(data: ImageData):
    request_id = str(uuid.uuid4())
    logger.info(f"Received request with {len(data.images)} images")

    best_result = None
    all_ages = []
    gender_votes = {"Man": 0, "Woman": 0}
    valid_embeddings = []

    for i, base64_image in enumerate(data.images):
        try:
            image_data = base64.b64decode(base64_image)
            nparr = np.frombuffer(image_data, np.uint8)
            img = cv2.imdecode(nparr, cv2.IMREAD_COLOR)

            # 이미지 전처리 수행
            preprocessed_img = preprocess_face(img)

            # 이미지 증강 수행
            augmented_images = augment_image(preprocessed_img)

            for aug_img in augmented_images:
                try:
                    # 얼굴 분석 수행 (DeepFace 사용)
                    analysis = DeepFace.analyze(aug_img, actions=['age', 'gender', 'embedding'])

                    if 'embedding' in analysis and analysis['embedding'] is not None:
                        age = analysis["age"]
                        gender = analysis["gender"]
                        embedding = analysis["embedding"]

                        all_ages.append(age)
                        gender_votes[gender] += 1
                        valid_embeddings.append(embedding)

                        logger.info(f"Augmented image processed successfully: Age {age}, Gender {gender}")

                except Exception as e:
                    logger.error(f"Error processing augmented image: {str(e)}")

        except Exception as e:
            logger.error(f"Error processing image {i+1}: {str(e)}")

    if not valid_embeddings:
        raise HTTPException(status_code=400, detail="No valid faces detected in any of the images")

    # 얼굴이 감지된 이미지의 임베딩만 사용하여 평균 임베딩 계산
    avg_embedding = np.mean(valid_embeddings, axis=0).tolist()

    best_result = AnalysisResult(
        age=int(np.mean(all_ages)),
        gender=max(gender_votes, key=gender_votes.get),
        is_face=True,
        encrypted_embedding=json.dumps(avg_embedding)
    )

    logger.info(f"Best face analysis result selected. Returning result.")
    return JSONResponse(content={"result": best_result.dict()})

@app.get("/fast/health")
async def health_check():
    return {"status": "healthy"}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("face_analysis_api:app", host="0.0.0.0", port=8000)
