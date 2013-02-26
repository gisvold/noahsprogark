from django.db import models


class Game(models.Model):
	company = models.CharField(max_length=255)
	title = models.CharField(max_length=255)
	
	def __unicode__(self):
		return self.title


class Term(models.Model):
	game = models.ForeignKey(Game)
	term = models.CharField(max_length=200)

	def __unicode__(self):
		return self.term


class Board(models.Model):
	game = models.ForeignKey(Game)
	terms = models.ManyToManyField(Term)