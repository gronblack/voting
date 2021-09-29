package com.github.gronblack.voting.to;

import com.github.gronblack.voting.model.HasIdAndEmail;
import com.github.gronblack.voting.util.validation.NoHtml;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserTo extends NamedTo implements HasIdAndEmail, Serializable {

    @Serial
    private static final long serialVersionUID = 7747547401459053473L;

    @Email
    @NotBlank
    @Size(max = 100)
    @NoHtml // https://stackoverflow.com/questions/17480809
    String email;

    @NotBlank
    @Size(min = 5, max = 20)
    String password;

    public UserTo(Integer id, String name, String email, String password) {
        super(id, name);
        this.email = email;
        this.password = password;
    }
}
