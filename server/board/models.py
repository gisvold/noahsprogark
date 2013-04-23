from django.db import models

from player.models import Player
from game.models import Game
from term.models import Term

class Board(models.Model):
    player = models.ForeignKey(Player)
    game = models.ForeignKey(Game)

    g = Game.objects.select_related('game__terms')
    # print g
    terms = g.all().order_by('?') #[:(g.board_size*g.board_size)] #models.ManyToManyField(Term, related_name='b+')
    active = models.IntegerField(max_length=1)

"""
class BoardTermRel(models.Model):
    term = models.ForeignKey(Term)
    game = models.ForeignKey(Game)
    board = models.ForeignKey(Board)
    order = models.IntegerField(default=1)
"""