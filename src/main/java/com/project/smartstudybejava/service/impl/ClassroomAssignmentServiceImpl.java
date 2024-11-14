package com.project.smartstudybejava.service.impl;

import com.project.smartstudybejava.entity.*;
import com.project.smartstudybejava.enumeration.EAssignmentStatus;
import com.project.smartstudybejava.exception.AppException;
import com.project.smartstudybejava.repository.*;
import com.project.smartstudybejava.service.ClassroomAssignmentService;
import com.project.smartstudybejava.util.ErrorCode;
import com.project.smartstudybejava.util.ResponseData;
import com.project.smartstudybejava.util.SuccessCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClassroomAssignmentServiceImpl implements ClassroomAssignmentService {

    ClassroomAssignmentRepository classroomAssignmentRepository;
    StudentAssignmentRepository studentAssignmentRepository;
    ClassroomRepository classroomRepository;
    ExamRepository examRepository;
    UserRepository userRepository;

    @Override
    public ClassroomAssignment assignToClassroom(Long classroomId, Long examId, String title, String description) {

        Classroom classroom = classroomRepository.findById(classroomId).orElseThrow(() ->
                new AppException(ErrorCode.CLASSROOM_NOT_FOUND.getCode(), ErrorCode.CLASSROOM_NOT_FOUND.getMessage()));
        Exam exam = examRepository.findById(examId).orElseThrow(() ->
                new AppException(ErrorCode.EXAM_NOT_FOUND.getCode(), ErrorCode.EXAM_NOT_FOUND.getMessage()));

        ClassroomAssignment classroomAssignment = new ClassroomAssignment();
        classroomAssignment.setClassroom(classroom);
        classroomAssignment.setExam(exam);
        classroomAssignment.setName(title);
        classroomAssignment.setDescription(description);
        classroomAssignment.setCreatedAt(LocalDateTime.now());

        classroomAssignment = classroomAssignmentRepository.save(classroomAssignment);

        List<User> students = userRepository.findByClassroom(classroom);
        for (User student : students) {
            StudentAssignment studentAssignment = new StudentAssignment();
            studentAssignment.setUser(student);
            studentAssignment.setExam(exam);
            studentAssignment.setName(title);
            studentAssignment.setDescription(description);
            studentAssignment.setCreatedAt(LocalDateTime.now());
            studentAssignment.setAssignmentStatus(EAssignmentStatus.NOT_SUBMIT);

            studentAssignmentRepository.save(studentAssignment);
        }

        return ResponseData.<ClassroomAssignment>builder()
                .message(SuccessCode.ASSIGN_SUCCESSFUL.getMessage())
                .data(classroomAssignment)
                .build().getData();

    }
}
