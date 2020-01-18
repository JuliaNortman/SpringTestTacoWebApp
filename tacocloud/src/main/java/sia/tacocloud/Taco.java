package sia.tacocloud;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
public class Taco {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "created_at")
	private Date createdAt;

	@NotNull
	@Size(min = 5, message = "Name must be at least 5 characters long")
	private String name;

	@ManyToMany(targetEntity = Ingredient.class)
	@JoinTable(joinColumns = { @JoinColumn(name = "taco") }, inverseJoinColumns = { @JoinColumn(name = "ingredient") })
	@NotNull
	@Size(min = 1, message = "You must choose at least 1 ingredient")
	private List<Ingredient> ingredients;

	@PrePersist
	void createdAt() {
		this.createdAt = new Date();
	}

}
