package pl.futurecollars.invoicing.db


import pl.futurecollars.invoicing.service.JsonService
import spock.lang.Specification
import spock.lang.Stepwise

@Stepwise
abstract class AbstractDatabaseTest extends Specification {

    List<WithId> itemList
    JsonService jsonService = new JsonService()

    abstract Database getDatabaseInstance()

    abstract List<WithId> getItemsList()

    Database<WithId> database

    def setup() {
        database = getDatabaseInstance()
        itemList = getItemsList()
        database.reset()

        assert database.getAll().isEmpty()
    }

    def saveItems() {
        itemList.forEach({ item -> database.save(item) })
        itemList = database.getAll()
    }

    def "should inject database instance"() {
        expect:
        database
    }

    def "should load items list"() {
        expect:
        itemList != null
        !itemList.isEmpty()
    }

    def "should return empty collection if there is no items in base"() {
        expect:
        database.getAll().isEmpty()
    }

    def "should return empty optional if there is no item with given id"() {
        expect:
        database.getById(1).isEmpty()
    }

    def "should return all saved items"() {
        setup:
        saveItems()

        when:
        List<WithId> savedItems = database.getAll()

        then:
        itemList.size() == savedItems.size()
        jsonService.objectToString(itemList) == jsonService.objectToString(savedItems)
    }

    def "should return item by id"() {
        setup:
        saveItems()
        Long id1 = itemList.get(0).getId()
        Long id2 = itemList.get(itemList.size() - 1).getId()

        when:
        WithId resultItem1 = database.getById(id1).get()
        WithId resultItem2 = database.getById(id2).get()

        then:
        jsonService.objectToString(resultItem1) == jsonService.objectToString(itemList.get(0))
        jsonService.objectToString(resultItem2) == jsonService.objectToString(itemList.get(itemList.size() - 1))
    }

    def "should return items without deleted one"() {
        given:
        saveItems()
        int itemsNumberBeforeDelete = database.getAll().size()
        long idToDelete = database.getAll().get(0).getId()

        when:
        database.delete(idToDelete)

        then:
        database.getAll().size() == itemsNumberBeforeDelete - 1
        database.getById(idToDelete).isEmpty()
    }

    def "can delete all items"() {
        setup:
        saveItems()

        when:
        database.getAll().forEach({ item -> database.delete(item.id) })

        then:
        database.getAll().isEmpty()
    }

    def "should throw illegalArgumentException if there is no id for update item"() {
        when:
        database.update(999999, itemList.get(0))

        then:
        thrown(IllegalArgumentException)
    }

    def "should throw illegalArgumentException if there is no id using delete"() {
        when:
        database.delete(999888)

        then:
        thrown(IllegalArgumentException)
    }
}
