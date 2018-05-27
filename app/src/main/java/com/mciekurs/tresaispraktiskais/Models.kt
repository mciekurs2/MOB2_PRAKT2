package com.mciekurs.tresaispraktiskais

/** Konstruktors, kurā glabājas json dati */
class UserRepos(val name: String, val size: String, val html_url: String, val owner: Owner)

class Owner(val avatar_url: String)