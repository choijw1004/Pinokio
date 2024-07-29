package com.example.pinokkio.common;

import com.example.pinokkio.api.pos.code.Code;
import com.example.pinokkio.api.pos.code.CodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InitService implements ApplicationListener<ContextRefreshedEvent> {

    private final CodeRepository codeRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
//        codeRepository.save(new Code("맥도날드"));
//        codeRepository.save(new Code("롯데리아"));
//        codeRepository.save(new Code("KFC"));
//        codeRepository.save(new Code("맘스터치"));
//        codeRepository.save(new Code("버거킹"));
    }
}
