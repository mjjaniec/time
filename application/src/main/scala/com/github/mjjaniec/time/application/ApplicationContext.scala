package com.github.mjjaniec.time.application

import com.github.mjjaniec.time.application.persistance.{WorkDayConfig, WorkTimeDao}

case class ApplicationContext(workTimeDao: WorkTimeDao,
                              workDayConfig: WorkDayConfig)