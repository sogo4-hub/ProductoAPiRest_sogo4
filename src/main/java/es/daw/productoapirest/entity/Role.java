package es.daw.productoapirest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;
//Relacion bidireccional. Role es el lado no propietario. Es el lado inverso (mapped)
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public Role() {
        users = new HashSet<User>();
    }

    public void addUser(User user) {
        users.add(user);
//        user.addRole(this);//Se me hacen llamdas infinitas!!! Cuidado
//        user.getRoles().add(this);//no hay llamadas ciclicas!!!! Porque estoy actualizando directamente la coleccion
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    @Override
    public String toString() {
        return "Rol{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}


