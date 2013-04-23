from django.db import models

from tastypie.utils.timezone import now

from company.models import Company
from term.models import Term

class Game(models.Model):
    company = models.ForeignKey(Company)
    title = models.CharField(max_length=255)
    board_size = models.IntegerField()
    create_date = models.DateTimeField(default=now)
    terms = models.ManyToManyField(Term, related_name='t+')

    def __unicode__(self):
        return self.title

"""
class GameRel(models.Model):
    term = models.ForeignKey(Term)
    game = models.ForeignKey(Game)
    order = models.IntegerField(default=1)
"""