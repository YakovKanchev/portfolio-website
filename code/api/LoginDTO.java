package api;

public class LoginDTO {
    //DTO (Data Transfer Object) POJO (Plain Old Java Object)
    private String email;
    private String password;
    private String domain;

    public LoginDTO(String email, String password, String domain) {
        this.email = email;
        this.password = password;
        this.domain = domain;
    }
}
