-- STUDENTS
INSERT INTO students (id, department) VALUES
                                          (1, 'CSE'),
                                          (2, 'CSE'),
                                          (3, 'ECE'),
                                          (4, 'MECH');

-- COMPANIES
INSERT INTO companies (id, name) VALUES
                                     (1, 'TCS'),
                                     (2, 'Amazon');

-- JOBS
INSERT INTO jobs (id, company_id, package) VALUES
                                               (1, 1, 3.5),
                                               (2, 2, 12.0);

-- APPLICATIONS
INSERT INTO applications (id, student_id, job_id, status) VALUES
                                                              (1, 1, 1, 'SELECTED'),
                                                              (2, 2, 2, 'APPLIED'),
                                                              (3, 3, 1, 'SELECTED'),
                                                              (4, 4, 1, 'REJECTED');
