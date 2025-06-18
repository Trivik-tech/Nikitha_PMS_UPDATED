package com.triviktech.utilities.email;

public class Message {

    public static final String REGISTRATION_SUBJECT = "Welcome %s! Your Registration is Successful";
    public static final String REGISTRATION_MESSAGE =
            "Dear %s,\n\n" +
                    "We are pleased to inform you that your registration has been successfully completed on %s.\n" +
                    "Your employee ID is %s. Please keep this ID safe for future reference.\n\n" +
                    "If you have any questions or need assistance, feel free to reach out to HR.\n\n" +
                    "Best regards,\n" +
                    "HR Team";

    public static final String OTP_SUBJECT = "OTP Verification Code for %s";

    public static final String OTP_MESSAGE =
            "Dear %s,\n\n" +
                    "Your One-Time Password (OTP) for verification is: %s\n" +
                    "This code is valid for the next %d minutes. Please do not share it with anyone.\n\n" +
                    "If you did not request this OTP, please contact support immediately.\n\n" +
                    "Regards,\n" +
                    "Security Team";


    public static final String PMS_SUBJECT = "PMS Cycle Initiated – Submit Your KRA/KPI by %s";
    public static final String PMS_MESSAGE =
            "Dear %s,\n\n" +
                    "We would like to inform you that the Performance Management System (PMS) cycle has been initiated for the duration: %s.\n" +
                    "Please login to the PMS portal and complete your KRA/KPI submission before %s.\n\n" +
                    "Your proactive participation is crucial for a fair and transparent review process.\n\n" +
                    "Warm regards,\n" +
                    "HR Department";

    public static final String KRA_KPI_SUBJECT_TO_MANAGER = "KRA/KPI Submitted by %s for Your Review";

    public static final String KRA_KPI_MESSAGE_TO_MANAGER =
            "Dear %s,\n\n" +
                    "This is to inform you that %s (Employee ID: %s) has successfully submitted their KRA/KPI.\n\n" +
                    "You may now review the submission in the PMS portal at your earliest convenience.\n\n" +
                    "Kindly provide appropriate weightage for each KRA and KPI, and proceed with the approval process.\n\n" +
                    "If you have any questions or need additional information, please feel free to reach out to the employee directly or contact HR.\n\n" +
                    "Best regards,\n" +
                    "HR Team";

    public static final String KRA_KPI_APPROVED_SUBJECT_TO_EMPLOYEE = "Your KRA/KPI Submission Has Been Approved";

    public static final String KRA_KPI_APPROVED_MESSAGE_TO_EMPLOYEE =
            "Dear %s,\n\n" +
                    "We are pleased to inform you that your submitted KRA/KPI has been approved by your manager.\n\n" +
                    "You may now proceed with your performance activities based on the approved objectives.\n\n" +
                    "If you have any questions or require further clarification, please reach out to your reporting manager.\n\n" +
                    "Best regards,\n" +
                    "HR Team";

    public static final String KRA_KPI_APPROVED_SUBJECT_TO_HR = "KRA/KPI Approved for %s – Initiate PMS";
    public static final String KRA_KPI_APPROVED_MESSAGE_TO_HR =
            "Dear HR Team,\n\n" +
                    "This is to notify you that the KRA/KPI submitted by %s (Employee ID: %s) has been successfully approved by their reporting manager.\n\n" +
                    "You may now initiate the PMS (Performance Management System) process for this employee.\n\n" +
                    "Please proceed with the next steps in accordance with the performance cycle guidelines.\n\n" +
                    "Regards,\n" +
                    "System Notification";

    public static final String SELF_APPRAISAL_SUBJECT_TO_MANAGER = "Self-Appraisal Completed by %s – Ready for Review";
    public static final String SELF_APPRAISAL_MESSAGE_TO_MANAGER =
            "Dear %s,\n\n" +
                    "This is to inform you that %s (Employee ID: %s) has successfully completed their self-appraisal.\n\n" +
                    "You may now review the submitted appraisal and proceed with your feedback and rating in the PMS portal.\n\n" +
                    "For any clarifications or discussions, please connect directly with the employee.\n\n" +
                    "Regards,\n" +
                    "HR Team";

    public static final String MANAGER_REVIEW_COMPLETED_SUBJECT_TO_HR = "PMS Cycle Completed for %s";
    public static final String MANAGER_REVIEW_COMPLETED_MESSAGE_TO_HR =
            "Dear HR Team,\n\n" +
                    "This is to inform you that the performance review for %s (Employee ID: %s) has been successfully completed by their reporting manager.\n\n" +
                    "The PMS cycle for this employee is now marked as complete. Please take the necessary next steps as per the performance management process.\n\n" +
                    "For any further details or clarifications, you may refer to the PMS portal or contact the respective manager.\n\n" +
                    "Regards,\n" +
                    "System Notification";

    public static final String EMPLOYEE_DETAILS_UPDATED_SUBJECT = "Your Profile Details Have Been Updated";
    public static final String EMPLOYEE_DETAILS_UPDATED_MESSAGE =
            "Dear %s,\n\n" +
                    "This is to inform you that your profile details have been successfully updated in the system.\n\n" +
                    "If you did not request these changes or notice any incorrect information, please contact the HR department immediately.\n\n" +
                    "Thank you for keeping your information up to date.\n\n" +
                    "Best regards,\n" +
                    "HR Team";







}
