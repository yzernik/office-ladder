package models

case class Ladder(id: Int,
                  name: String,
                  domain: String,
                  creator: String,
                  created: Int,
                  active: Boolean)

case class LadderInput(name: String)
