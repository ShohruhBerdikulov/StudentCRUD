package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Student;
import uz.pdp.appjparelationships.repository.StudentRepository;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentRepository studentRepository;

    //1. VAZIRLIK
    @GetMapping("/forMinistry")
    public Page<Student> getStudentListForMinistry(@RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAll(pageable);
        return studentPage;
    }

    //2. UNIVERSITY
    @GetMapping("/forUniversity/{universityId}")
    public Page<Student> getStudentListForUniversity(@PathVariable Integer universityId,
                                                     @RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        return studentRepository.findAllByGroup_Faculty_UniversityId(universityId, pageable);
    }

    //3. FACULTY DEKANAT

    @GetMapping("/forFaculty/{facultyId}")
    public Page<Student> getStudentListForFaculty(@PathVariable Integer facultyId, @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return studentRepository.findAllByGroup_Faculty_Id(facultyId, pageable);
    }

    //4. GROUP OWNER
    @GetMapping("/forFaculty/{groupId}")
    public Page<Student> getStudentListForGroup(@PathVariable Integer groupId, @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return studentRepository.findAllByGroupId(groupId, pageable);
    }

    @PutMapping
    public String addStudent(@Valid @RequestBody Student newStudent) {
        if (!studentRepository.existsByAddress_IdAndGroup_Id(newStudent.getAddress().getId(), newStudent.getGroup().getId())) {
            return "malumotlaringiz chala yoki bunday student mavjud to'liq qilib jonating ";
        }
        studentRepository.save(newStudent);
        return "successfully added";
    }

    @PostMapping("/{id}")
    public String updateStudent(@PathVariable Integer id, @RequestBody Student StudentDto) {
        Optional<Student> byId = studentRepository.findById(id);
        if (!byId.isPresent()) {
            return "bunday id mavjud emas";
        }
        Student saved = byId.get();
        Student newStudent = new Student();
        int a = 0, b = 0;
        if (StudentDto.getAddress().getId() != null) {
            saved.setAddress(newStudent.getAddress());
            a++;
        }
        if (StudentDto.getFirstName() != null) saved.setFirstName(newStudent.getFirstName());
        if (StudentDto.getLastName() != null) saved.setLastName(newStudent.getLastName());
        if (StudentDto.getGroup() != null) {
            saved.setGroup(newStudent.getGroup());
            b++;
        }

        if ((a == 1 || b == 1) && !studentRepository.existsByAddress_IdAndGroup_Id(saved.getAddress().getId(), saved.getGroup().getId())) {
            return "bunday student mavjud";
        }
        saved.setGroup(newStudent.getGroup());
        studentRepository.save(saved);
        return "successfully added";
    }


}
