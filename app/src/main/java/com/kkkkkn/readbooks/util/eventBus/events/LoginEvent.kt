package com.kkkkkn.readbooks.util.eventBus.events

import com.kkkkkn.readbooks.util.eventBus.EventMessage

class LoginEvent(var message: EventMessage, var name: String, var password: String)