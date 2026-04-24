package domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "tbl_user_preferences")
open class UserPreference(
    @Id
    @Column(name = "preference_id", columnDefinition = "INTEGER")
    open var id: Long = 1,
    @Column(name = "is_light_theme", columnDefinition = "INTEGER")
    open var isLightTheme: Boolean = true,
) {
    constructor() : this(1, true)
}
