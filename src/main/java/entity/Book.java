package entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static entity.Book.QUERY_BY_RATING;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Entity
@Table(name = "BOOK")
@NamedQueries({
        @NamedQuery(name = QUERY_BY_RATING,
                query = "select b from Book b WHERE b.avgRating between ?1 and ?2"),
        @NamedQuery(name = Book.QUERY_COUNT_BY_RATING,
                query = "select count(*) from Book b WHERE b.avgRating between ?1 and ?2"),
//                query = "select count(b) from Book b WHERE b.avgRating between 2 and 5")
})
public class Book implements Serializable {

    public static final String QUERY_BY_RATING = "Book.eq.ratinq";
    public static final String QUERY_COUNT_BY_RATING = "Book.count.eq.ratinq";

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="book_id_sequence")
    @SequenceGenerator(name="book_id_sequence", allocationSize = 1, sequenceName="BOOK_SEQ")
    private Long id;

    @Column(name = "ISBN")
    @Pattern(regexp = "[0-9]{10,18}", message = "ISBN must have minimum 10, maximum 18 numbers")
    private String isbn;

    @Column(name = "NAME")
    @Pattern(regexp = "[a-zA-Z0-9_.-]{3,50}", message = "Name must contain minimum 3 maximum 50 characters")
    private String name;

    @Column(name = "PUBLISHER")
    @Pattern(regexp = "[a-zA-Z0-9_.-]{3,50}", message = "PUBLISHER must contain minimum 3 maximum 50 characters")
    private String publisher;

    @Column(name = "PUBLISH_YEAR")
//    @Range(min = 1000, max = 2017)
    private int publishYear;

    @Column(name = "AVG_RATING")
    private Float avgRating;

    @Column(name = "CREATE_DATE")
    private Date crateDate; // TODO: createDate

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "JOIN_BOOK_AUTHOR",
            joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn (name = "author_id")})
    private List<Author> authors = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade=CascadeType.ALL)
    private List<Reviews> reviews;

    @Override
    public String toString() {
        return "Book";
    }
}
