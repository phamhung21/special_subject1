package myDBApp;

import java.sql.ResultSet;
import java.sql.SQLException;
import utils.TextIO;
import wk3.db.DBApp;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @overview: this program help user solve some problems in FIT
 * 
 * @author Pham Hung
 * 
 *
 */
public class CourseManApp {

	private static boolean check;
	private static Connection conn;
	private static PreparedStatement pst;
	private static ResultSet rs;
	private static DBApp dba;
	private static String resultOfSelect;
	private static boolean result;
	private static String userDir;
	private static String file;
	private static String sql;
	private static String fileChar;

	/**
	 * the main method is used for connecting to database (via DBApp) and display menu of function.
	 * @param args
	 */
	public static void main(String[] args) {
		connect();
		menu();
	}

	/**
	 * this method is used for connecting to database
	 * 
	 * @effect: 
	 * 	if connect successfully
	 * 		return true 
	 * 	else
	 * 		return false
	 */
	public static void connect() {
		dba = new DBApp(DBApp.DRIVER_POSTGRESQL);

		String dbName = "courseman";
		String userName = "postgres";
		String password = "hung2109";

		boolean ok = dba.connect(dbName, userName, password);
		if (!ok)
			System.exit(0);
		System.out.println();
	}

	/**
	 * this method contain the main menu of Course Man App
	 * @effect:
	 * 	if choice is invalid
	 * 		return message and prompt user re-enter
	 * 	else
	 * 		invoke methods
	 */
	public static void menu() {
		TextIO.putln("========================");
		TextIO.putln("  Main Menu Of Course Man App");
		TextIO.putln("1: Manage students");
		TextIO.putln("2: Manage course");
		TextIO.putln("3: Manage enrolments");
		TextIO.putln("4: Reports");
		TextIO.putln("5: Exit program");
		TextIO.putln("========================");
		TextIO.putln();

		int function = TextIO.getlnInt();
		while (function < 1 || function > 5) {
			System.err.print("Invalid option, please re-enter: ");
			function = TextIO.getlnInt();
		}
		switch (function) {
		case 1:
			manageStudent();
			break;
		case 2:
			manageCourse();
			break;
		case 3:
			manageEnrolments();
			break;
		case 4:
			reports();
			break;
		case 5:
			System.out.println("<3 bye bye <3");
			System.exit(0);
			break;
		}

	}

	/**
	 * 
	 * this method is used to manage student
	 * 		case 1: add a new student
	 * 		case 2: edit student's infor 
	 * 		case 3: delete a student
	 * 		case 4: show list of all student
	 * 		case 5: back to main menu
	 * 
	 * @effect
	 * 	if valid
	 * 		execute function
	 * 	else
	 * 		error message
	 */
	public static void manageStudent() {

		TextIO.putln("=======================");
		TextIO.putln("	   Manage Students");
		TextIO.putln("1: Add a new student");
		TextIO.putln("2: Edit information of an existing student");
		TextIO.putln("3: Delete a student");
		TextIO.putln("4: Display a list of all student");
		TextIO.putln("5: Back to main menu");
		TextIO.putln("========================");
		TextIO.putln();

//		prompt user enter choice for manage student
		int choice = TextIO.getlnInt();
		while (choice < 1 || choice > 5) {
			System.err.print("Invalid option, please re-enter: ");
			choice = TextIO.getlnInt();
		}

		switch (choice) {

//		 case 1: add a new student
		case 1:

//			prompt user enter student's id
			TextIO.put("Enter student's id: ");
			int studentID = TextIO.getlnInt();

//			check whether student's id is existed or not
			while (!checkStudentID(studentID) || studentID < 1) {
				System.err.print("ID is existed/invalid, please re-enter: ");
				studentID = TextIO.getlnInt();
				checkStudentID(studentID);
			}

//		 	prompt user enter student's first name and validate it
			TextIO.put("Enter student's first name: ");
			String firstName = TextIO.getln();
			while (firstName.length() < 1 || firstName.startsWith(" ") || firstName.contains("'")) {
				System.err.print("Please re-enter student's first name (not start with a space): ");
				firstName = TextIO.getln();
			}

//			prompt user enter student's last name and validate it
			TextIO.put("Enter student's last name: ");
			String lastName = TextIO.getln();
			while (lastName.length() < 1 || lastName.startsWith(" ") || lastName.contains("'")) {
				System.err.print("Please re-enter student's last name (not start with a space): ");
				lastName = TextIO.getln();
			}

//			prompt user enter student's address and validate it
			TextIO.put("Enter student's address: ");
			String address = TextIO.getln();
			while (address.length() < 1 || address.startsWith(" ") || address.contains("'")) {
				System.err.print("Please re-enter student's address (not start with a space): ");
				address = TextIO.getln();
			}

//			prompt user enter student's day of birth and validate it
			TextIO.put("Enter student's day of birth: ");
			int date = TextIO.getlnInt();
			while (date < 1 || date > 31) {
				System.err.print("Date isn't existed, please re-enter: ");
				date = TextIO.getlnInt();
			}

//			prompt user enter student's month of birth and validate it
			TextIO.put("Enter student's month of birth: ");
			int month = TextIO.getlnInt();
			while (month < 1 || month > 12) {
				System.err.print("Month isn't existed, please re-enter: ");
				month = TextIO.getlnInt();
			}

//			prompt user enter year of birth and validate it
			TextIO.put("Enter student's year of birth: ");
			int year = TextIO.getlnInt();
			while (year < 1900 || year > 2020) {
				System.err.print("invalid, please re-enter: ");
				year = TextIO.getlnInt();
			}

//			check validate date of birth
			String dateOfBirth = checkDate(date, month, year);

//			execute insert a new student into database
			String sql = "INSERT INTO student VALUES (" + studentID + ", '" + standardizeString(firstName) + "', '"
					+ standardizeString(lastName) + "', '" + standardizeString(address) + "', '" + dateOfBirth + "')";
			insert(sql);
			TextIO.putln();
			break;

//		case 2: edit information of student
		case 2:

			TextIO.putln("=======================");
			TextIO.putln("	   Edit student's information");
			TextIO.putln("1: Edit student's first name");
			TextIO.putln("2: Edit student's last name");
			TextIO.putln("3: Edit student's address");
			TextIO.putln("4: Edit student's date of birth");
			TextIO.putln("5: Back to main menu");
			TextIO.putln("========================");
			TextIO.putln();

//			prompt user enter option to edit
			TextIO.put("Your option: ");
			int infor = TextIO.getlnInt();
			while (infor < 1 || infor > 5) {
				System.err.print("Invalid option, please re-enter: ");
				infor = TextIO.getlnInt();
			}

			if (infor == 5) {

				menu();
				break;
			} else {

				TextIO.put("First, please enter student's id who you want to edit: ");
				studentID = TextIO.getlnInt();

//				check student's id to make sure it is existed
				while (checkStudentID(studentID)) {
					System.err.print("ID is not existed, please re-enter: ");
					studentID = TextIO.getlnInt();
					checkStudentID(studentID);
				}

				switch (infor) {

//				edit student's first name
				case 1:
					TextIO.put("Enter student's first name: ");
					firstName = TextIO.getln();
					while (firstName.length() < 1 || firstName.startsWith(" ") || firstName.contains("'")) {
						System.err.print("Please re-enter student's first name (not start with a space): ");
						firstName = TextIO.getln();
					}
					sql = "UPDATE student SET firstname = '" + standardizeString(firstName) + "' WHERE studentid = "
							+ studentID;
					update(sql);
					break;

//				edit student's last name
				case 2:
					TextIO.put("Enter student's last name: ");
					lastName = TextIO.getln();
					while (lastName.length() < 1 || lastName.startsWith(" ") || lastName.contains("'")) {
						System.err.print("Please re-enter student's last name (not start with a space): ");
						lastName = TextIO.getln();
					}
					sql = "UPDATE student SET lastname = '" + standardizeString(lastName) + "' WHERE studentid = "
							+ studentID;
					update(sql);
					break;

//				edit student's address
				case 3:
					TextIO.put("Enter student's address: ");
					address = TextIO.getln();
					while (address.length() < 1 || address.startsWith(" ") || address.contains("'")) {
						System.err.print("Please re-enter student's address (not start with space: ");
						address = TextIO.getln();
					}
					sql = "UPDATE student SET address = '" + standardizeString(address) + "' WHERE studentid = "
							+ studentID;
					update(sql);
					break;

//				edit student's date of birth
				case 4:

					// edit day of birth
					TextIO.put("Enter student's day of birth: ");
					date = TextIO.getlnInt();
					while (date < 1 || date > 31) {
						System.err.print("date isn't existed, please re-enter: ");
						date = TextIO.getlnInt();
					}

//					edit month of birth
					TextIO.put("Enter student's month of birth: ");
					month = TextIO.getlnInt();
					while (month < 1 || month > 12) {
						System.err.print("Month isn't existed, please re-enter: ");
						month = TextIO.getlnInt();
					}

//					edit month of birth
					TextIO.put("Enter student's year of birth: ");
					year = TextIO.getlnInt();
					while (year < 1900 || year > 2020) {
						System.err.print("Invalid, please re-enter: ");
						year = TextIO.getlnInt();
					}

//					validate and create new student's date of birth
					dateOfBirth = checkDate(date, month, year);

					sql = "UPDATE student SET dateofbirth = '" + dateOfBirth + "' WHERE studentid = " + studentID;
					update(sql);
					break;
				}
				break;
			}

//		case 3: delete a student
		case 3:
			TextIO.put("Enter student's id you want to delete: ");
			studentID = TextIO.getlnInt();

			while (checkStudentID(studentID)) {
				System.err.print("ID is not existed, please re-enter: ");
				studentID = TextIO.getlnInt();
				checkStudentID(studentID);
			}

			sql = "DELETE FROM student WHERE studentid = " + studentID;
			delete(sql);
			break;

//		case 4: Display a list of all student
		case 4:
			sql = "SELECT * FROM student";
			resultOfSelect = dba.select(sql);

			userDir = System.getProperty("user.dir");
			fileChar = System.getProperty("file.separator");
			file = userDir + fileChar + "students.html";

			TextIO.writeFile(file);
			TextIO.putln("<b>Result for query:</b><br>" + sql);
			TextIO.putln("<p>");
			TextIO.putln(resultOfSelect);

			select(sql);
			break;

//		case 5: back to main menu
		case 5:
			menu();
			break;
		}
		menu();
	}

	/**
	 * this method used for managing course
	 * 
	 *		case 1: add a new course
	 *		case 2: edit course's information
	 *		case 3: delete a course
	 *		case 4: show list of all course
	 *		case 5: back to main menu
	 *
	 *@effect 
	 *	if valid
	 *		execute function
	 *	else
	 *		error message
	 *
	 */
	public static void manageCourse() {

		TextIO.putln("========================");
		TextIO.putln("     Manage Courses");
		TextIO.putln("1: Add a new course");
		TextIO.putln("2: Edit information of an existing course");
		TextIO.putln("3: Delete a course");
		TextIO.putln("4: Display a list of all course");
		TextIO.putln("5: Back to main menu");
		TextIO.putln("========================");
		TextIO.putln();

		TextIO.put("Enter your choice: ");
		int choice = TextIO.getlnInt();
		while (choice < 1 || choice > 5) {
			System.err.print("Invalid choice, please re-enter: ");
			choice = TextIO.getlnInt();
		}

		switch (choice) {

//		case 1: Add a new course
		case 1:
			TextIO.put("Enter course's id: ");
			String courseID = TextIO.getln().toUpperCase();

//			check length of course id
			while (courseID.length() != 3 || courseID.contains("'")) {
				System.err.print("Course's id is no more or less than 3 character, please re-enter: ");
				courseID = TextIO.getln().toUpperCase();
			}

//			check duplicate course id
			while (!checkCourseID(courseID)) {
				System.err.print("Course's id is existed, please re-enter: ");
				courseID = TextIO.getln().toUpperCase();
				checkCourseID(courseID);
			}

			TextIO.put("Enter course's name: ");
			String name = TextIO.getln();
			while (name.length() < 1 || name.startsWith(" ") || name.contains("'")) {
				System.err.print("Please re-enter course's name: ");
				name = TextIO.getln();
			}

			TextIO.put("Enter course's prerequisites: ");
			String prerequisites = TextIO.getln().toUpperCase();

//			/validate prerequisites	
			if (prerequisites.equalsIgnoreCase("-")) {
				sql = "INSERT INTO course VALUES ('" + courseID + "','" + standardizeString(name) + "','"
						+ prerequisites + "')";
				insert(sql);
			} else {
				while (!(prerequisites.equalsIgnoreCase("-")) && checkCourseID(prerequisites)) {
					System.err.print("Prerequisites is invalid, please re-enter: ");
					prerequisites = TextIO.getln().toUpperCase();
				}

				sql = "INSERT INTO course VALUES ('" + courseID + "','" + standardizeString(name) + "','"
						+ prerequisites + "')";
				insert(sql);
			}
			break;

//		Edit course's information
		case 2:
			TextIO.putln("========================");
			TextIO.putln("     Edit Courses's Information");
			TextIO.putln("1: Edit Course's name");
			TextIO.putln("2: Edit Course's prerequisites");
			TextIO.putln("========================");
			TextIO.putln();

			TextIO.put("First, please enter Course's id you want to edit: ");
			courseID = TextIO.getln().toUpperCase();

//			check courseid
			while (checkCourseID(courseID)) {
				System.err.print("Course's id isn't existed/valid, please re-enter: ");
				courseID = TextIO.getln().toUpperCase();
			}

			TextIO.put("Enter your option (1 or 2): ");
			int option = TextIO.getlnInt();
			while (option < 1 || option > 2) {
				System.err.print("Invalid option, please re-enter: ");
				option = TextIO.getlnInt();
			}

			switch (option) {
//			case 1: edit course's name
			case 1:
				TextIO.put("Enter Course's name: ");
				name = TextIO.getln();
				while (name.length() < 1 || name.startsWith(" ") || name.contains("'")) {
					System.err.print("Please re-enter course's name (not start with a space): ");
					name = TextIO.getln().toUpperCase();
				}
				sql = "UPDATE course SET name ='" + standardizeString(name) + "' WHERE courseid = '" + courseID + "'";
				update(sql);
				break;

//			case 2: edit course's prerequisites
			case 2:
				TextIO.put("Enter Course's prerequisites: ");
				prerequisites = TextIO.getln().toUpperCase();
				if (prerequisites.equalsIgnoreCase("-")) {
					sql = "UPDATE course SET prerequisites= '" + prerequisites + "' WHERE courseid= '" + courseID + "'";
					update(sql);
				} else {
					while (!(prerequisites.equalsIgnoreCase("-")) && checkCourseID(prerequisites)) {
						System.err.print("Prerequisites is invalid, please re-enter: ");
						prerequisites = TextIO.getln().toUpperCase();
					}
					sql = "UPDATE course SET prerequisites= '" + prerequisites + "' WHERE courseid= '" + courseID + "'";
					update(sql);
				}
			}
			break;

//		delete a course
		case 3:
			TextIO.put("Enter Course's id you want to delete: ");
			courseID = TextIO.getln().toUpperCase();
			while (checkCourseID(courseID)) {
				System.err.print("Course's id isn't existed/valid, please re-enter: ");
				courseID = TextIO.getln().toUpperCase();
				checkCourseID(courseID);
			}

			sql = "DELETE FROM course WHERE courseid = '" + courseID + "'";
			delete(sql);
			break;

//		case 4: display a list of all course
		case 4:
			sql = "SELECT * FROM course";
			userDir = System.getProperty("user.dir");
			fileChar = System.getProperty("file.separator");
			file = userDir + fileChar + "courses.html";

			String result = dba.select(sql);
			TextIO.writeFile(file);
			TextIO.putln("<b>Result for query:</b><br>" + sql);
			TextIO.putln("<p>");
			TextIO.putln(result);
			select(sql);
			break;

//		return to main menu of course man app
		case 5:
			menu();
			break;
		}
		menu();
	}

	/**
	 * this method manage enrolments
	 * 	case 1: Add an enrolment
	 *  case 2: enter grade for student's enrolment
	 *  case 3: show list of all enrolment
	 *  case 4: show list of all enrolment with final grade descending
	 *  case 5: back to menu
	 *  @effects:
	 *  	if user choose the correct choice
	 *  		invoke the chosen method
	 *  	else
	 *  		display error message and prompt user re-enter
	 */
	public static void manageEnrolments() {
		TextIO.putln("========================");
		TextIO.putln("    Manage Enrolments");
		TextIO.putln("1: Add an enrolment");
		TextIO.putln("2: Update final grade for a student's enrolment");
		TextIO.putln("3: Display a table of all enrolments");
		TextIO.putln("4: Display a list of all enrolments sorted in descending order of grade");
		TextIO.putln("5: Back to main menu");
		TextIO.putln("========================");
		TextIO.putln();

		TextIO.put("Enter Your choice: ");
		int choice = TextIO.getlnInt();
		while (choice < 1 || choice > 5) {
			System.err.println("Invalid choice, please re=enter");
			choice = TextIO.getlnInt();
		}

		switch (choice) {
//		case 1: add a new enrolment
		case 1:

			TextIO.put("Enter student's id: ");
			int studentID = TextIO.getlnInt();

//			check student's existed or not
			while (checkStudentID(studentID)) {
				System.err.print("Student's id is not existed, please re-enter: ");
				studentID = TextIO.getlnInt();
			}

			String course;
			TextIO.put("Enter course's id: ");
			String courseID = TextIO.getln().toUpperCase();
//			check validate course's id
			while (checkCourseID(courseID)) {
				System.err.print("Course's id is invalid, please re-enter: ");
				courseID = TextIO.getln().toUpperCase();
			}
//			lấy ra prerequisites để kiểm tra
			String pre = takePrequisites(courseID);

//			nếu prerequisites bằng - thì kiểm tra đã học khóa học cần đăng kí hay chưa
//			 		nếu học rồi thì chạy dòng if
//					nếu chưa học thì chạy dòng else
			if (pre.equals("-")) {
				System.err.println("Khóa này không có prerequisites, kiểm tra " + courseID + " xem đã học hay chưa");
				if (!checkEnrolCourseID(studentID, courseID)) {
					System.out.println("Loading....100%");
					System.err.println("Đã học " + courseID + ". Không cần enroll nữa");
					System.err.println();
					break;
				} else {
					System.out.println("Loading....100%");
					System.err.println("Chưa học " + courseID + ". Enroll ngay đi");
					System.out.println();
				}
			}

//			 nếu prerequisites là id của một môn học khác 
//				kiểm tra đã học prerequisites chưa
//				nếu chưa học thì hiện ra thông báo không cho enroll
//				nếu học rồi thì chạy dòng else thứ 2 và kiểm tra đã từng học môn học đang đăng kí hay chưa

			else {

//				kiểm tra đã học khóa pre hay chưa
//				chưa học thì chạy dòng if và đưa ra thông báo
//				học rồi thì chạy dòng else

				if (checkEnrolCourseID(studentID, pre)) {
					TextIO.putln();
					System.err.println("học sinh chưa học " + pre + " nên không thể enroll vào " + courseID);
					System.err.print("Re-start the program");
					TextIO.putln();
					break;
				} else {
					System.err.println("Đã học " + pre + ", kiểm tra student đã học " + courseID + " hay chưa");
					if (!checkEnrolCourseID(studentID, courseID)) {
						System.out.println("Loading....100%");
						System.err.println("Đã học " + courseID);
						break;
					} else {
						System.out.println("Loading....100%");
						System.err.println("Chưa học " + courseID + ". Enroll ngay đi");
						course = courseID;
					}
				}
			}
			course = courseID;

//			Enter course's semester
			System.out.println();
			TextIO.put("Enter semester (1-8): ");
			int semester = TextIO.getlnInt();
			while (semester < 1 || semester > 8) {
				System.err.print("Semester is invalid, please re-enter: ");
				semester = TextIO.getlnInt();
			}

//			Enter course's final grade and validate it
			TextIO.put("Enter final grade (E, F, G or P): ");
			String grade = TextIO.getln().toUpperCase();
			while (!(grade.equals("F") || grade.equals("P") || grade.equals("G") || grade.equals("E"))) {
				System.err.println("Invalid final grade, please re-enter: ");
				grade = TextIO.getln().toUpperCase();
			}

			sql = "INSERT INTO enrolment VALUES (" + studentID + ",'" + course + "'," + semester + ",'" + grade + "')";

			insert(sql);
			break;

//		case 2: Update final grade for a student's enrolment
		case 2:
			TextIO.put("Enter student's id: ");
			int student = TextIO.getlnInt();

			while (checkStudentID(student)) {
				System.err.print("Invalid student's id, please re-enter: ");
				student = TextIO.getlnInt();
			}

			TextIO.put("Enter course's id: ");
			courseID = TextIO.getln().toUpperCase();
			while (checkCourseID(courseID)) {
				System.err.print("Course's id isn't existed, please re-enter: ");
				courseID = TextIO.getln().toUpperCase();
			}

			while (checkEnrolment(student, courseID)) {
				System.err.print("Student doesn't enroll into course " + courseID + " please check and re-enter: ");
				courseID = TextIO.getln().toUpperCase();
			}

			TextIO.put("Enter final grade (E, F, G or P): ");
			grade = TextIO.getln().toUpperCase();

			while (!(grade.equals("F") || grade.equals("P") || grade.equals("G") || grade.equals("E"))) {
				System.err.print("Invalid final grade, please re-enter: ");
				grade = TextIO.getln().toUpperCase();
			}

			sql = "UPDATE enrolment SET finalgrade ='" + grade + "' WHERE student =  " + student + " AND course = '"
					+ courseID + "'";

			update(sql);

			break;
//		case 3: display a table of all enrolment
		case 3:
			userDir = System.getProperty("user.dir");
			fileChar = System.getProperty("file.separator");
			file = userDir + fileChar + "enrols.html";

			sql = "select st.studentid, st.firstname, st.lastname, er.course, cr.name, er.finalgrade from enrolment as er, student as st, course as cr where er.student = st.studentid and er.course = cr.courseid ";
			resultOfSelect = dba.select(sql);

			TextIO.writeFile(file);
			TextIO.putln("<b>Result for query:</b><br>" + sql);
			TextIO.putln("<p>");
			TextIO.putln(resultOfSelect);

			select(sql);
			break;

//		case 4: display a table of all enrolment sorted in descending final grade
		case 4:

			userDir = System.getProperty("user.dir");
			fileChar = System.getProperty("file.separator");
			file = userDir + fileChar + "enrols_sorted.html";

			sql = "SELECT * FROM enrolment " + "ORDER BY (case finalgrade " + "when 'E' then 1 " + "when 'G' then 2 "
					+ "when 'P' then 3 " + "when 'F'then 4 " + "else 5 end)";

			resultOfSelect = dba.select(sql);
			TextIO.writeFile(file);
			TextIO.putln("<b>Result for query:</b><br>" + sql);
			TextIO.putln("<p>");
			TextIO.putln(resultOfSelect);

			select(sql);
			break;

//		case 5: back to main menu
		case 5:
			menu();
			break;
		}
		menu();
	}

	/**
	 * this method is used for report function
	 * @effects:
	 * 	if user choose correct choice:
	 * 		invoke the function which user choose
	 * 	else
	 * 		display error message and prompt user choose again
	 */
	public static void reports() {
		TextIO.putln("========================");
		TextIO.putln("        Reports         ");
		TextIO.putln("1: Display a list of all  courses of a given student");
		TextIO.putln("2: Display all student of a given course");
		TextIO.putln("3: Display a list of all student who failed at least one course");
		TextIO.putln("4: Back to main menu");
		TextIO.putln("========================");
		TextIO.putln();

		TextIO.put("Enter your choice: ");
		int choice = TextIO.getlnInt();
		while (choice < 1 || choice > 4) {
			System.err.print("Invalid choice, please re-enter: ");
			choice = TextIO.getlnInt();
		}

		switch (choice) {
		// case 1: display a list all course of a student
		case 1:
			TextIO.put("Enter studen's id you want display all course: ");
			int id = TextIO.getlnInt();
			while (checkStudentID(id)) {
				System.err.print("ID isn't existed, please re-enter: ");
				id = TextIO.getlnInt();
				checkStudentID(id);
			}
			userDir = System.getProperty("user.dir");
			fileChar = System.getProperty("file.separator");
			file = userDir + fileChar + "my_enrols.html";

			sql = "SELECT st.studentid, st.firstname, st.lastname, er.course, cr.name, er.semester, er.finalgrade "
					+ "FROM enrolment as er, student as st, course as cr  " + "WHERE er.student =" + id
					+ " AND st.studentid= " + id + " AND er.course = cr.courseid";
			String result = dba.select(sql);

			TextIO.writeFile(file);
			TextIO.putln("<b>Result for query:</b><br>" + sql);
			TextIO.putln("<p>");
			TextIO.putln(result);

			select(sql);
			break;

//		case 2: display students of a given course
		case 2:
			TextIO.put("Enter course's id you want to display student: ");
			String courseid = TextIO.getln().toUpperCase();

			while (checkCourseID(courseid)) {
				System.err.print("Course ID isn't existed, please re-enter: ");
				courseid = TextIO.getln().toUpperCase();
				checkCourseID(courseid);
			}

			userDir = System.getProperty("user.dir");
			fileChar = System.getProperty("file.separator");
			file = userDir + fileChar + "course_enrols.html";
			sql = "SELECT st.studentid, st.firstname, st.lastname, er.course "
					+ "FROM enrolment as er, course as cr , student as st "
					+ "WHERE er.student = st.studentid AND er.course = '" + courseid + "' AND cr.courseid = '"
					+ courseid + "'";

			result = dba.select(sql);

			TextIO.writeFile(file);
			TextIO.putln("<b>Result for query:</b><br>" + sql);
			TextIO.putln("<p>");
			TextIO.putln(result);

			select(sql);

			break;

//		display student failed at least one course
		case 3:

			userDir = System.getProperty("user.dir");
			fileChar = System.getProperty("file.separator");
			file = userDir + fileChar + "fails.html";
			sql = "select st.studentid, st.firstname, st.lastname, er.course, er.finalgrade from enrolment as er,  student as st where er.student = st.studentid and finalgrade ='F'";

			result = dba.select(sql);

			TextIO.writeFile(file);
			TextIO.putln("<b>Result for query:</b><br>" + sql);
			TextIO.putln("<p>");
			TextIO.putln(result);

			select(sql);
			break;

//		back to main menu
		case 4:
			menu();
			break;
		}
		menu();
	}

	/**
	 * this method is used for execute SELECT statement
	 * 
	 * @param sql
	 * 
	 * @effect:
	 * 	return string
	 */
	public static void select(String sql) {

		try {
			System.out.println("Executing query: " + sql);
			System.out.println("Written result to file: " + file);
			System.out.println();
			TextIO.writeStandardOutput();
		} catch (Exception e) {
			// e.getMessage();
		}
	}

	/**
	 * this method is used for execute INSERT statement
	 * 
	 * @effect:
	 * 	if success
	 * 		return message insert success
	 * 	else
	 * 		return message insert unsuccess
	 * 
	 * @param sql
	 */
	public static void insert(String sql) {

		try {
			TextIO.putln();
			System.out.print("Executing query: " + sql);

			result = dba.insert(sql);

			System.out.println();
			if (result == true) {
				System.out.println("Insert success");
			} else {
				System.err.println("Insert unsuccess");
			}
			TextIO.putln();
			TextIO.writeStandardOutput();
		} catch (Exception e) {

		}

	}

	/**
	 * this method is used for execute UPDATE statement
	 * 
	 * @param sql
	 * 
	 * @effect:
	 * 	if success
	 * 		return message UPDATE success
	 * 	else
	 * 		return message UPDATE unsuccess
	 */
	public static void update(String sql) {

		try {
			TextIO.putln();
			System.out.print("Executing query: " + sql);
			result = dba.update(sql);
			System.out.println();
			if (result == true) {
				System.out.println("Update success!");
			} else {
				System.out.println("Update unsuccess!");
			}
			TextIO.writeStandardOutput();
		} catch (Exception e) {
//			 e.printStackTrace();
		}
	}

	/**
	 * this method is used for execute DELETE statement
	 * 
	 * @param sql
	 * 
	 * @effect:
	 * 	if success
	 * 		return message DELETE success
	 * 	else
	 * 		return message DELETE unsuccess
	 */
	public static void delete(String sql) {

		try {
			TextIO.putln();
			System.out.print("Executing query: " + sql);
			result = dba.delete(sql);
			System.out.println();
			if (result == true) {
				System.out.println("Delete success!");
			} else {
				System.out.println("Delete unsuccess!");
			}
			TextIO.writeStandardOutput();
		} catch (Exception e) {
//			 e.printStackTrace();
		}
	}

	/**
	 * 
	 * this method is used for check date
	 *
	 * @param date
	 * @param month
	 * @param year
	 * 
	 * @effect:
	 * 	if validate date, month, year
	 * 		return date/ month/ year
	 * 	else
	 * 		return error message, prompt user re-enter
	 */
	public static String checkDate(int date, int month, int year) {
		String dateofbirth;
		if (year % 4 == 0) {
			if (month == 2) {
				while (date < 1 || date > 29) {
					System.err.print("Date isn't exist in this month, please re-enter: ");
					date = TextIO.getlnInt();
				}
			} else if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10
					|| month == 12) {
				while (date < 1 || date > 31) {
					System.err.print("Date isn't existed in this month, please re-enter: ");
					date = TextIO.getlnInt();
				}
			} else {// (month == 2 || month == 4 || month == 6 || month == 9 || month == 11) {
				while (date < 1 || date > 30) {
					System.err.print("Date isn't existed in this month, please re-enter: ");
					date = TextIO.getlnInt();
				}
			}
		} else {
			if (month == 2) {
				while (date < 1 || date > 28) {
					System.err.print("Date isn't exist in this month, please re-enter: ");
					date = TextIO.getlnInt();
				}
			} else if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10
					|| month == 12) {
				while (date < 1 || date > 31) {
					System.err.print("Date isn't existed in this month, please enter: ");
					date = TextIO.getlnInt();
				}
			} else { // (month == 2 || month == 4 || month == 6 || month == 9 || month == 11) {
				while (date < 1 || date > 30) {
					System.err.print("Date isn't existed in this month, please re-enter: ");
					date = TextIO.getlnInt();
				}
			}
		}
		dateofbirth = date + "/" + month + "/" + year;
		return dateofbirth;
	}

	/**
	 * this method is used for check duplicate or valid student's id
	 * 
	 * @param id
	 * 
	 * @return
	 * 	if student's id is existed
	 * 		return false
	 * 	else
	 * 		return true 
	 */
	public static boolean checkStudentID(int id) {
		check = true;
		String sql = "SELECT studentid FROM student";
		try {
			conn = dba.getConnection();
			pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				int studentid = rs.getInt("studentid");
				if (studentid == id) {
					check = false;
				}
			}
		} catch (SQLException e) {
			System.out.println("ERROR: " + e.getMessage());
		}
		return check;
	}

	/**
	 * 
	 * this method is used for check duplicate or valid student's id
	 * @param id
	 * 
	 * @return
	 * 	if course's id is invalid
	 * 		return false
	 * 	else
	 * 		return true
	 */
	public static boolean checkCourseID(String id) {
		check = true;
		sql = "SELECT courseid FROM course";
		try {
			conn = dba.getConnection();
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				String course = rs.getString("courseid");
				if (course.equals(id)) {
					check = false;
				}
			}
		} catch (SQLException e) {
			System.out.println("ERROR: " + e.getMessage());
		}
		return check;
	}

	
	/**
	 * this method is used for to standardize the string input such as name, address
	 * @param str
	 * @return 
	 * 		the string which is standardized
	 */
	public static String standardizeString(String str) {
		String[] text2 = str.split("\\s+");

		String subString = "";
		for (int i = 0; i < text2.length; i++) {
			// System.out.print(text2[i] + " ");

			String text4 = String.valueOf(text2[i].charAt(0)).toUpperCase();
			String text5 = text2[i].substring(1).toLowerCase();
			subString += text4 + text5;

			if (i < text2.length) {
				subString += " ";
			}
		}
		return subString;
	}
	
	/**
	 * this method is used for checking that whether student enrolled into a course or not
	 * @param student
	 * @param courseID
	 * @return
	 */
	public static boolean checkEnrolment(int student, String courseID) {

		check = true;
		sql = "SELECT course FROM enrolment WHERE student = " + student;
		try {
			conn = dba.getConnection();
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				String id = rs.getString("course");
				if (id.equals(courseID)) {
					check = false;
				}
			}
		} catch (SQLException e) {
			// e.printStackTrace();
		}
		return check;
	}
	
	/**
	 * this method is used for taking prerequisites of a course to check enroll
	 * @param courseID
	 * @return
	 * 		return prerequisites of a course (e.g: prerequisite of PPL is IPG)
	 */
	public static String takePrequisites(String courseID) {
		String pre = null;
		sql = "SELECT prerequisites FROM course WHERE courseid = '" + courseID + "'";
		try {
			conn = dba.getConnection();
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				pre = rs.getString("prerequisites");
			}

		} catch (SQLException e) {

		}
		return pre;
	}
	
	/**
	 * this method is used for checking whether student enroll into a course or not
	 * @param studentID
	 * @param pre
	 * @return
	 * 		if student have enrolled already, return true
	 * 		else return false
	 */
	public static boolean checkEnrolCourseID(int studentID, String pre) {
		check = true;
		sql = "SELECT * FROM enrolment WHERE student = " + studentID + "AND course = '" + pre + "'";
		try {
			conn = dba.getConnection();
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				String prerequisites = rs.getString("course");
				if (prerequisites.equals(pre)) {
					check = false;
				}
			}
		} catch (SQLException e) {

		}
		return check;
	}
}