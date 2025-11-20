package es.daw.productoapirest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter  // ← Solo getters
@Setter  // ← Solo setters
//@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 4, unique = true)
    private String codigo;

    @Column(nullable = false, length = 255)
    private String nombre;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    // Relación muchos a uno con Fabricante
    @ManyToOne(optional = false)
    @JoinColumn(name = "codigo_fabricante", nullable = false)
    private Fabricante fabricante;

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                '}';
    }
}
