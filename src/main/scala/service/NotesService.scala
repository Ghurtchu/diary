package service

import db._
import model.*
import zio.*

import java.time.Instant
import java.util.Date

object NotesService:

  val notesRepository: CRUD[Note] = NotesRepository

  def add(note: Note): Task[Boolean] = notesRepository add note

  def getAll: UIO[List[Note]] = notesRepository.getAll

  def getById(id: Int): Task[Option[Note]] = notesRepository getById id

  def delete(id: Int): Task[Boolean] = notesRepository delete id

  def update(id: Int, note: Note): Task[Boolean] = notesRepository.update(id, note)

