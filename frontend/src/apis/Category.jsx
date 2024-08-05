import axios from './Axios';

/*
변수 네이밍 규칙
0. 카멜 케이스로 쓴다.
1. 행위를 맨 앞에 쓴다.
2. 가져오는 대상을 다음에 쓴다.
3. 가져오는 대상이 리스트라면 복수형으로 쓴다.
*/

export const deleteCategory = async (posId, categoryId) => {
  try {
    const response = await axios.delete('/api/pos/:posId/categories/:categoryId', {
      params: { posId: posId, categoryId: categoryId },
    });
    return response.data;
  } catch (error) {
    console.error('delete category failed:', error);
    throw error;
  }
};

export const getCategories = async (posId) => {
  try {
    const response = await axios.get(`/api/pos/${posId}/categories`);
    return response.data;
  } catch (error) {
    console.error('get categories failed:', error);
    throw error;
  }
};

export const createCategory = async (posId, name) => {
  try {
    const response = await axios.post('/api/pos/:posId/categories', {
      params: { posId: posId },
      name: name,
    });
    return response.data;
  } catch (error) {
    console.error('create categories failed:', error);
    throw error;
  }
};
