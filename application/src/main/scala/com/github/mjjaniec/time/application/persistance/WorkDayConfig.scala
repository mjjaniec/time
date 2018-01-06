package com.github.mjjaniec.time.application.persistance

import java.time.Duration

case class WorkDayConfig(workday: Duration,
                         tolerance: Duration,
                         overtimeNotificationInterval: Duration)