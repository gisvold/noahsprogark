from django.db import models

from player.models import Player
from game.models import Game
from term.models import Term

class Board(models.Model):
    player = models.ForeignKey(Player)
    game = models.ForeignKey(Game)
    terms = models.TextField(blank=True, null=True)
    active = models.BooleanField(default=True)

    def save(self, *args, **kwargs):
        termList = []
        g = Game.objects.get(id=self.game.id)
        for t in g.terms.all().order_by('?')[:(g.board_size*g.board_size)]:
            termList.append(t.__str__())
        print termList
        self.terms = ",".join(termList)
        super(Board, self).save()
