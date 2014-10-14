package models.kendo

import play.api.mvc.QueryStringBindable


  /*
   * filter[logic]:and
   * filter[filters][0][field]:x
   * filter[filters][0][operator]:eq
   * filter[filters][0][value]:y
   */
  case class FilterCondition(field: String, operator: String, value: String)
  case class Filter(logic: String, filters: List[FilterCondition])
  
object Filter {
 implicit def bindableFilter(implicit stringBinder: QueryStringBindable[String]) = new QueryStringBindable[Filter] {
      override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, Filter]] = {
        for {
          logic <- stringBinder.bind(key + "[logic]", params)
          filters <- bindFilters(key + "[filters]", params) //QueryStringBindable.bindableList[String].bind(key + "[filters]", params)
        } yield {
          (logic, filters) match {
            case (Right(logic), filters) => Right(Filter(logic, filters))
            case _ => Left("Unable to bind a Filter")
          }
        }
      }
      override def unbind(key: String, pager: Filter): String = {
        stringBinder.unbind(key + "[logic]", pager.logic) + "&"
      }
    }

    implicit def bindableFilterCondition(implicit stringBinder: QueryStringBindable[String]) = new QueryStringBindable[FilterCondition] {
      override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, FilterCondition]] = {
        for {
          field <- stringBinder.bind(key + "[field]", params)
          operator <- stringBinder.bind(key + "[operator]", params)
          value <- stringBinder.bind(key + "[value]", params)
        } yield {
          (field, operator, value) match {
            case (Right(field), Right(operator), Right(value)) => Right(FilterCondition(field, operator, value))
            case _ => Left("Unable to bind a FilterCondition")
          }
        }
      }
      override def unbind(key: String, condition: FilterCondition): String = {
        stringBinder.unbind(key + "[field]", condition.field) + "&" + stringBinder.unbind(key + "[operator]", condition.operator) + "&" + stringBinder.unbind(key + "[value]", condition.value)
      }
    }

    private def bindFilters(key: String, params: Map[String, Seq[String]]): Option[List[FilterCondition]] = {
      //      for {
      //        values <- params.get(key).toList
      //        rawValue <- values
      //        bound <- implicitly[QueryStringBindable[FilterCondition]].bind(key, Map(key -> Seq(rawValue)))
      //        value <- bound.right.toOption
      //      } yield value
      //      val size = params.keySet.groupBy(k => k.replaceAll("\\[\\d+\\]", "")).mapValues(_.size).values.max
      //      for (i <- 0 until  (size - 1)) {
      //        
      //      }
      for {
        condition <- bindableFilterCondition.bind(key + "[0]", params)
      } yield {
        condition match {
          case Right(condition) => List(condition)
          case _ => Nil
        }
      }
    }
}