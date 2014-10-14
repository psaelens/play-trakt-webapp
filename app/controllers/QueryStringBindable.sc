package controllers

object QueryStringBindable {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  val params = Map(
  "filter[logic]" -> List("and"),
  "filter[filters][0][field]" -> List("CategoryID"),
  "filter[filters][0][operator]" -> List("eq"),
  "filter[filters][0][value]" -> List("1") ,
  "filter[filters][1][field]" -> List("CategoryID"),
  "filter[filters][1][operator]" -> List("eq"),
  "filter[filters][1][value]" -> List("1")
  )                                               //> params  : scala.collection.immutable.Map[String,List[String]] = Map(filter[f
                                                  //| ilters][0][operator] -> List(eq), filter[filters][0][field] -> List(Category
                                                  //| ID), filter[filters][1][operator] -> List(eq), filter[logic] -> List(and), f
                                                  //| ilter[filters][1][field] -> List(CategoryID), filter[filters][0][value] -> L
                                                  //| ist(1), filter[filters][1][value] -> List(1))
  import controllers.Application.Filter._
  
  val filter = bindableFilter.bind("filter", params)
                                                  //> filter  : Option[Either[String,controllers.Application.Filter]] = Some(Right
                                                  //| (Filter(and,List(FilterCondition(CategoryID,eq,1)))))
  

	def p(entry:(String, List[String])):Boolean = entry._1.startsWith("filter[filters]")
                                                  //> p: (entry: (String, List[String]))Boolean
	
	params.count(p)                           //> res0: Int = 6
	
	
	params.keySet.groupBy(k => k.replaceAll("\\[\\d+\\]", "")).mapValues(_.size).values.max
                                                  //> res1: Int = 2
	
}