from django.db import models

from tastypie.utils.timezone import now

from company.models import Company
from term.models import Term
from player.models import Player

class Game(models.Model):
    company = models.ForeignKey(Company)
    golden_word = models.ForeignKey(Term, null=True, blank=True)
    title = models.CharField(max_length=255)
    board_size = models.IntegerField()
    create_date = models.DateTimeField(default=now)
    terms = models.ManyToManyField(Term, related_name='t+')
    bingo_value = models.IntegerField(default=0)
    bingo_leader = models.ForeignKey(Player, null=True, blank=True)

    def __unicode__(self):
        return self.title
