package dev.luiiscarlos.academ_iq_api.core.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ErrorMessages {

    public static final String UNEXPECTED_ERROR = "An unexpected error occurred";


    public static final String USER_NOT_FOUND = "User with ID '%d' not found";

    public static final String USER_NOT_FOUND_BY_NAME = "User with name '%s' not found";

    public static final String USER_AVATAR_NOT_FOUND = "User avatar";

    public static final String USER_NOT_VERIFIED = "User account '%s' not verified";


    public static final String COURSE_NOT_FOUND = "Course with ID '%d' not found";

    public static final String COURSE_NOT_FOUND_BY_NAME = "Course with name '%s' not found";

    public static final String CATEGORY_NOT_FOUND = "Category '%s' does not exist";


    public static final String REFRESH_TOKEN_NOT_FOUND = "Refresh token not found";

    public static final String REFRESH_TOKEN_NOT_ASSOCIATED_WITH_USER = "No refresh token associated with user '%s'";


    public static final String FILE_MISSING = "File upload failed: no file provided";

    public static final String FILE_TYPE_NOT_SUPPORTED = "Upload failed: unsupported file type '%s'";

    public static final String FILE_TOO_LARGE = "Upload failed: file exceeds the max allowed size of %d MB";

    public static final String FILES_NOT_FOUND = "No files found";

    public static final String FILE_NOT_FOUND = "File with ID '%d' not found";

    public static final String FILE_NOT_FOUND_BY_NAME = "File with name '%s' not found";

    public static final String FILE_DELETION = "Failed to delete file with ID '%d'";

    public static final String FILE_DELETION_BY_NAME = "Failed to delete file with name '%s'";

    public static final String INVALID_CONTENT_TYPE = "Failed to upload file with content type %s";


    public static final String FILE_NOT_FOUND_CLOUDINARY = "File not found in Cloudinary with public ID '%s'";

    public static final String FILE_NOT_FOUND_CLOUDINARY_BY_NAME = "File not found in Cloudinary with name '%s'";

    public static final String FILE_DELETION_CLOUDINARY = "Failed to delete file from Cloudinary with public ID '%s'";

    public static final String FILE_DELETION_CLOUDINARY_BY_NAME = "Failed to delete file from Cloudinary with name '%s'";

    public static final String FILE_UPLOAD_CLOUDINARY = "Failed to upload file to Cloudinary with public ID '%s'";

    public static final String FILE_UPLOAD_CLOUDINARY_BY_NAME = "Failed to upload file to Cloudinary with name '%s'";


    public static final String RESOURCE_GENERATION = "Failed to generate resource for file with ID '%d'";

    public static final String RESOURCE_GENERATION_BY_NAME = "Failed to generate resource for file with name '%s'";


    public static final String ACCESS_DENIED = "Access denied";

    public static final String INSUFFICIENT_PERMISSIONS = "Access denied: insufficient permissions";

    public static final String ROLE_REQUIRED = "Access denied: '%s' role required";

    public static final String NOT_OWNER_OF_RESOURCE = "User '%s' does not own this %s";


    public static final String INVALID_CREDENTIALS = "Invalid username or password";


    public static final String EXPIRED_TOKEN = "Authentication failed: token expired";

    public static final String INVALID_TOKEN = "Authentication failed: invalid token";

    public static final String REQUIRED_TOKEN = "Authentication failed: token";

    public static final String MALFORMED_TOKEN = "Authentication failed: Malformed token";

    public static final String INVALID_TOKEN_TYPE = "Authentication failed: Invalid token type";

    public static final String TOKEN_MISMATCH = "Authentication failed: Refresh token does not match stored token";

    public static final String TOKEN_GENERATION = "Failed to generate JSON Web Token";

    public static final String TOKEN_SUBJECT_NOT_FOUND = "Token subject '%s'  does not match any user record";


    public static final String COURSE_ALREADY_EXISTS = "Course '%s' already exists";

    public static final String USERNAME_ALREADY_TAKEN = "Username '%s' is already in use";


    public static String formatted(String template, Object... args) {
        return String.format(template, args);
    }

}
