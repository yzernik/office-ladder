package officeladder

import japgolly.scalajs.react._, vdom.all._
import scala.scalajs.js

import org.scalajs.jquery.jQuery

object PictureAppExample {

  case class Picture(id: String, url: String, src: String, title: String, favorite: Boolean = false)

  case class State(pictures: List[Picture], favourites: List[Picture])

  type PicClick = (String, Boolean) => Unit

  class Backend(t: BackendScope[Unit, State]) {

    def onPicClick(id: String, favorite: Boolean) = {
      if (favorite) {
        val newPics = t.state.pictures.map(p => if (p.id == id) p.copy(favorite = false) else p)
        val newFavs = t.state.favourites.filter(p => p.id != id)
        t.modState(_ => State(newPics, newFavs))
      } else {
        var newPic: Picture = null
        val newPics = t.state.pictures.map(p => if (p.id == id) {
          newPic = p.copy(favorite = true); newPic
        } else p)
        val newFavs = t.state.favourites.+:(newPic)
        t.modState(_ => State(newPics, newFavs))
      }
    }
  }

  val picture = ReactComponentB[(Picture, PicClick)]("picture")
    .render(P => {
      val (p, b) = P
      div(if (p.favorite) cls := "picture favorite" else cls := "picture", onClick --> b(p.id, p.favorite))(
        img(src := p.src, title := p.title))
    })
    .build

  val pictureList = ReactComponentB[(List[Picture], PicClick)]("pictureList")
    .render(P => {
      val (list, b) = P
      div(`class` := "pictures")(
        if (list.isEmpty) span("Loading Pics..")
        else {
          list.map(p => picture.withKey(p.id)((p, b)))
        })
    })
    .build

  val favoriteList = ReactComponentB[(List[Picture], PicClick)]("favoriteList")
    .render(P => {
      val (list, b) = P
      div(`class` := "favorites")(
        if (list.isEmpty) span("Click an image to mark as  favorite")
        else {
          list.map(p => picture.withKey(p.id)((p, b)))
        })
    })
    .build

  val PictureApp = ReactComponentB[Unit]("PictureApp")
    .initialState(State(Nil, Nil))
    .backend(new Backend(_))
    .render((_, S, B) => {
      div(
        h1("Popular Instagram Pics"),
        pictureList((S.pictures, B.onPicClick)),
        h1("Your favorites"),
        favoriteList((S.favourites, B.onPicClick)))
    })
    .componentDidMount(scope => {
      // make ajax call here to get pics from instagram
      import scalajs.js.Dynamic.{ global => g }
      val url = "https://api.instagram.com/v1/media/popular?client_id=642176ece1e7445e99244cec26f4de1f&callback=?"
      val fn = (result: js.Dynamic) => {
        if (result != js.undefined && result.data != js.undefined) {
          val data = result.data.asInstanceOf[js.Array[js.Dynamic]]
          val pics = data.toList.map(item => Picture(item.id.toString, item.link.toString, item.images.low_resolution.url.toString, if (item.caption != null) item.caption.text.toString else ""))
          scope.modState(_ => State(pics, Nil))
        }
      }
      jQuery.get(url, Nil, fn, Nil)
    })
    .buildU

  val content = PictureApp()

}