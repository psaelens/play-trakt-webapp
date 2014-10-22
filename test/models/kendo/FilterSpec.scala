package models.kendo

import org.scalatest.FlatSpec
import org.scalatest.Matchers

class FilterSpec extends FlatSpec with Matchers {

  "JQuery params" should "be bindable" in {

    val params = Map(
      "filter[logic]" -> List("and"),
      "filter[filters][0][field]" -> List("CategoryID"),
      "filter[filters][0][operator]" -> List("eq"),
      "filter[filters][0][value]" -> List("1"),
      "filter[filters][1][field]" -> List("CategoryID"),
      "filter[filters][1][operator]" -> List("eq"),
      "filter[filters][1][value]" -> List("1"))

    import models.kendo.Filter._

    val filter = queryStringBinder.bind("filter", params)

    filter.getOrElse(fail("")).isRight should be (true)
    
    val actualFilter = filter.get.right.get
    
    actualFilter.logic should be ("and")
    
    actualFilter.filters should have size 2
  }

}