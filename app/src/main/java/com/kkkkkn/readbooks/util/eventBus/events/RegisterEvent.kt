package com.kkkkkn.readbooks.util.eventBus.events

import com.kkkkkn.readbooks.util.eventBus.EventMessage

class RegisterEvent(var message: EventMessage, var name: String, var password: String)