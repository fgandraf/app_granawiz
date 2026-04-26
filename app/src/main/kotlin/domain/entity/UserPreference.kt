package domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "tbl_user_preferences")
class UserPreference(
    @Id
    @Column(name = "preference_id", columnDefinition = "INTEGER") var id: Long = 1,
    @Column(name = "is_light_theme", columnDefinition = "INTEGER") var isLightTheme: Boolean = true,
    @Column(name = "currency_symbol") var currencySymbol: String = "Brazilian Real (R$)",
) {
    constructor() : this(1, true, "Brazilian Real (R$)")
}
