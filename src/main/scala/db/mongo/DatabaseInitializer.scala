package db.mongo

import zio.RIO

trait DatabaseInitializer:

  def initialize(DBConfig: DBConfig): RIO[DataSource, Unit]
