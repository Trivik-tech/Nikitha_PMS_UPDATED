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

    public static final String PMS_MESSAGE =
            "Dear %s,\n\n" +
                    "We would like to inform you that the Performance Management System (PMS) cycle has been initiated for the duration: %s.\n" +
                    "Please login to the PMS portal and complete your KRA/KPI submission before %s.\n\n" +
                    "Your proactive participation is crucial for a fair and transparent review process.\n\n" +
                    "Warm regards,\n" +
                    "HR Department";


}
