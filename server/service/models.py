import hashlib
import time
from tastypie.utils.timezone import now
from django.db import models
from django.template.defaultfilters import slugify


class Player(models.Model):
    name = models.CharField(max_length=200)
    sessionId = models.CharField(max_length=50)

    def save(self, *args, **kwargs):
        if not self.sessionId:
            self.sessionId = hashlib.md5(self.name + str(time.time())).hexdigest()
        return super(Player, self).save(*args, **kwargs)



class Term(models.Model):
    term = models.CharField(max_length=200)
    slug = models.SlugField()

    def __unicode__(self):
        return self.term

    def save(self, *args, **kwargs):
        if not self.slug:
            self.slug = slugify(self.term)

        return super(Term, self).save(*args, **kwargs)


class Company(models.Model):
    name = models.CharField(max_length=200)

    def __unicode__(self):
        return self.name

    def save(self, *args, **kwargs):
        return super(Company, self).save(*args, **kwargs)


class Game(models.Model):
    company = models.ForeignKey(Company)
    title = models.CharField(max_length=255)
    slug = models.SlugField()
    terms = models.ManyToManyField(Term, through='GameRel')
    create_date = models.DateTimeField(default=now)

    def __unicode__(self):
        return self.title

    def save(self, *args, **kwargs):
        if not self.slug:
            self.slug = slugify(self.title)

        return super(Game, self).save(*args, **kwargs)

class Board(models.Model):
    player = models.ForeignKey(Player)
    game = models.ForeignKey(Game)

    def save(self, *args, **kwargs):
        return super(Board, self).save(*args, **kwargs)



class GameRel(models.Model):
    term = models.ForeignKey(Term)
    game = models.ForeignKey(Game)
    order = models.IntegerField(default=1)
