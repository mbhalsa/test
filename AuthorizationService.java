public class AuthorizationService {
    public static boolean isAdmin(User currentUser) {
        if (currentUser == null) {
            return false;
        }
        return currentUser.getRole().equalsIgnoreCase("ADMIN");
    }
    public static boolean isDeveloper(User currentUser) {
        if (currentUser == null) {
            return false;
        }
        return currentUser.getRole().equalsIgnoreCase("Developer");
    }
}