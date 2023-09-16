package com.net128.test.entitysort.model.uni

import javax.persistence.*

@Entity
data class University(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String,
    @OneToMany(mappedBy = "university")
    val faculties: List<Faculty> = mutableListOf()
)

@Entity
data class Faculty(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String,
    @ManyToOne
    @JoinColumn(name = "university_id")
    val university: University,
    @OneToMany(mappedBy = "faculty")
    val departments: List<Department> = mutableListOf()
)

@Entity
data class Department(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String,
    @ManyToOne
    @JoinColumn(name = "faculty_id")
    val faculty: Faculty,
    @OneToMany(mappedBy = "department")
    val courses: List<Course> = mutableListOf()
)

@Entity
data class Course(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String,
    val description: String,
    val credits: Int,
    @ManyToOne
    @JoinColumn(name = "department_id")
    val department: Department,
    @ManyToMany(mappedBy = "courses")
    val students: List<Student> = mutableListOf()
)

@Entity
data class Student(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val dateOfBirth: String,  // This could also be a Date type
    val enrollmentDate: String, // Same as above
    val gradeAverage: Double,
    @ManyToMany
    @JoinTable(
        name = "student_course",
        joinColumns = [JoinColumn(name = "student_id")],
        inverseJoinColumns = [JoinColumn(name = "course_id")]
    )
    val courses: List<Course> = mutableListOf(),
    @ManyToOne
    @JoinColumn(name = "advisor_id")
    val advisor: Professor?
)

@Entity
data class Professor(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val firstName: String,
    val lastName: String,
    val specialization: String,
    @OneToMany(mappedBy = "advisor")
    val advisees: List<Student> = mutableListOf()
)

@Entity
data class Staff(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val firstName: String,
    val lastName: String,
    val position: String,
    val salary: Double
)

@Entity
data class Library(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String,
    @OneToMany(mappedBy = "library")
    val books: List<Book> = mutableListOf()
)

@Entity
data class Book(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val title: String,
    val author: String,
    val isbn: String,
    @ManyToOne
    @JoinColumn(name = "library_id")
    val library: Library
)

@Entity
data class Club(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String,
    @ManyToMany
    @JoinTable(
        name = "student_club",
        joinColumns = [JoinColumn(name = "club_id")],
        inverseJoinColumns = [JoinColumn(name = "student_id")]
    )
    val members: List<Student> = mutableListOf()
)
