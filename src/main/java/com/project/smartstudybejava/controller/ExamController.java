package com.project.smartstudybejava.controller;

import com.project.smartstudybejava.dto.req.ExamRequest;
import com.project.smartstudybejava.dto.res.ExamResponse;
import com.project.smartstudybejava.entity.Exam;
import com.project.smartstudybejava.service.ExamService;
import com.project.smartstudybejava.util.ErrorCode;
import com.project.smartstudybejava.util.ResponseData;
import com.project.smartstudybejava.util.SuccessCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/exam")
public class ExamController {
    ExamService examService;

    @PostMapping()
    public ResponseData<Exam> createExam(@ModelAttribute ExamRequest examRequest) {
        try {
            Exam exam = examService.createExam(examRequest);
            return ResponseData.<Exam>builder()
                    .code(SuccessCode.CREATE_EXAM_SUCCESSFUL.getCode())
                    .message(SuccessCode.CREATE_EXAM_SUCCESSFUL.getMessage())
                    .data(exam)
                    .build();
        } catch (IOException e) {
            return ResponseData.<Exam>builder()
                    .code(ErrorCode.CREATE_EXAM_FAILED.getCode())
                    .message(ErrorCode.CREATE_EXAM_FAILED.getMessage())
                    .build();
        }
    }
    @GetMapping
    public ResponseData<List<Exam>> getAllExams() {
        return ResponseData.<List<Exam>>builder()
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(examService.getAllExams())
                .build();
    }

    @GetMapping("/{examId}")
    public ResponseData<ExamResponse> getExamsByExamId(@PathVariable Long examId) {
        return ResponseData.<ExamResponse>builder()
                .message(SuccessCode.GET_EXAM_SUCCESSFUL.getMessage())
                .data(examService.getExamByExamId(examId))
                .build();
    }

}
