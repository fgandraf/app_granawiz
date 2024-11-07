package application.category.usecases

import domain.entity.Subcategory
import infrastructure.config.transactional

class DeleteSubcategoryUseCase {
    fun execute(subcategory: Subcategory) {
        transactional { session ->
            session.createMutationQuery(
                "UPDATE Transaction t SET t.subcategory = null WHERE t.subcategory = :sc"
            ).setParameter("sc", session.getReference(Subcategory::class.java, subcategory.id))
             .executeUpdate()
            session.createMutationQuery(
                "DELETE FROM Subcategory s WHERE s.id = :id"
            ).setParameter("id", subcategory.id)
             .executeUpdate()
        }
    }
}
