package com.project.smartstudybejava.service.impl;
import com.project.smartstudybejava.dto.req.QuestionUpdateRequest;
import com.project.smartstudybejava.entity.Answer;
import com.project.smartstudybejava.entity.ExpandContent;
import com.project.smartstudybejava.entity.Question;
import com.project.smartstudybejava.exception.AppException;
import com.project.smartstudybejava.repository.AnswerRepository;
import com.project.smartstudybejava.repository.QuestionRepository;
import com.project.smartstudybejava.service.QuestionService;
import com.project.smartstudybejava.util.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class QuestionServiceImpl implements QuestionService {
    QuestionRepository questionRepository;
    AnswerRepository answerRepository;

    @Override
    @Transactional
    public Question updateQuestion(QuestionUpdateRequest questionUpdateRequest) {
        Question question = questionRepository.findById(questionUpdateRequest.getQuestionId())
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND.getCode(),
                        ErrorCode.QUESTION_NOT_FOUND.getMessage()));

        question.setContent(questionUpdateRequest.getQuestionContent());

        saveAnswer(questionUpdateRequest.getFirstAnswerId(), questionUpdateRequest.getFirstAnswerContent(),
                questionUpdateRequest.isCorrectFirst());
        saveAnswer(questionUpdateRequest.getSecondAnswerId(), questionUpdateRequest.getSecondAnswerContent(),
                questionUpdateRequest.isCorrectSecond());
        saveAnswer(questionUpdateRequest.getThirdAnswerId(), questionUpdateRequest.getThirdAnswerContent(),
                questionUpdateRequest.isCorrectThird());
        saveAnswer(questionUpdateRequest.getFourthAnswerId(), questionUpdateRequest.getFourthAnswerContent(),
                questionUpdateRequest.isCorrectFourth());

        questionRepository.save(question);
        return question;
    }

    private void saveAnswer(Long answerId, String content, boolean isCorrect) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AppException(ErrorCode.ANSWER_NOT_FOUND.getCode(),
                        ErrorCode.ANSWER_NOT_FOUND.getMessage()));
        answer.setContent(content);
        answer.setIsCorrect(isCorrect);
        answerRepository.save(answer);
    }
}
