package officeladder

import japgolly.scalajs.react._, vdom.prefix_<^._

object ProductTableExample {

  case class Product(name: String, price: Double, category: String, stocked: Boolean)

  case class State(filterText: String, inStockOnly: Boolean)

  class Backend($: BackendScope[_, State]) {
    def onTextChange(e: ReactEventI) =
      $.modState(_.copy(filterText = e.target.value))
    def onCheckBox(e: ReactEvent) =
      $.modState(s => s.copy(inStockOnly = !s.inStockOnly))
  }

  val ProductCategoryRow = ReactComponentB[String]("ProductCategoryRow")
    .render(category => <.tr(<.th(^.colSpan := 2, category)))
    .build

  val ProductRow = ReactComponentB[Product]("ProductRow")
    .render(P =>
      <.tr(
        <.td(<.span(!P.stocked ?= ^.color.red, P.name)),
        <.td(P.price)))
    .build

  def productFilter(s: State)(p: Product): Boolean =
    p.name.contains(s.filterText) &&
      (!s.inStockOnly || p.stocked)

  val ProductTable = ReactComponentB[(List[Product], State)]("ProductTable")
    .render(P => {
      val (products, state) = P
      val rows = products.filter(productFilter(state))
        .groupBy(_.category).toList
        .flatMap {
          case (cat, ps) =>
            ProductCategoryRow.withKey(cat)(cat) :: ps.map(p => ProductRow.withKey(p.name)(p))
        }
      <.table(
        <.thead(
          <.tr(
            <.th("Name"),
            <.th("Price"))),
        <.tbody(
          rows))
    })
    .build

  val SearchBar = ReactComponentB[(State, Backend)]("SearchBar")
    .render(P => {
      val (s, b) = P
      <.form(
        <.input(
          ^.placeholder := "Search Bar ...",
          ^.value := s.filterText,
          ^.onChange ==> b.onTextChange),
        <.p(
          <.input(
            ^.tpe := "checkbox",
            ^.onClick ==> b.onCheckBox,
            "Only show products in stock")))
    })
    .build

  val FilterableProductTable = ReactComponentB[List[Product]]("FilterableProductTable")
    .initialState(State("", false))
    .backend(new Backend(_))
    .render((P, S, B) =>
      <.div(
        ^.className := "col-lg-4",
        SearchBar((S, B)),
        ProductTable((P, S)))).build

  val products = List(
    Product("FootBall", 49.99, "Sporting Goods", true),
    Product("Baseball", 9.99, "Sporting Goods", true),
    Product("basketball", 29.99, "Sporting Goods", false),
    Product("ipod touch", 99.99, "Electronics", true),
    Product("iphone 5", 499.99, "Electronics", true),
    Product("Nexus 7", 199.99, "Electronics", true))

  val content = FilterableProductTable(products)

}