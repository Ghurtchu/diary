package db

import model.Note
import zio.UIO

trait NoteCRUD extends CRUD[Note]{
  def getNotesByUserId(userId: Int): UIO[List[Note]]
}
