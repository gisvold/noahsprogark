from django.contrib.auth.models import User, Group
from api.models import Board, Game, Term
from rest_framework import generics
from rest_framework.decorators import api_view
from rest_framework.reverse import reverse
from rest_framework.response import Response
from api.serializers import UserSerializer, GroupSerializer, BoardSerializer, GameSerializer, TermSerializer

@api_view(['GET'])
def api_root(request, format=None):
    """
    The entry endpoint of our API.
    """
    return Response({
    	'games': reverse('game-list', request=request),
        'boards': reverse('board-list', request=request),
        'terms': reverse('term-list', request=request),
    })

class UserList(generics.ListCreateAPIView):
    model = User
    serializer_class = UserSerializer

class UserDetail(generics.RetrieveUpdateDestroyAPIView):
    model = User
    serializer_class = UserSerializer

class GroupList(generics.ListCreateAPIView):
    model = Group
    serializer_class = GroupSerializer

class GroupDetail(generics.RetrieveUpdateDestroyAPIView):
    model = Group
    serializer_class = GroupSerializer

class BoardList(generics.ListCreateAPIView):
    model = Board
    serializer_class = BoardSerializer

class BoardDetail(generics.RetrieveUpdateDestroyAPIView):
    model = Board
    serializer_class = BoardSerializer

class GameList(generics.ListCreateAPIView):
    model = Game
    serializer_class = GameSerializer

class GameDetail(generics.RetrieveUpdateDestroyAPIView):
    model = Game
    serializer_class = GameSerializer

class TermList(generics.ListCreateAPIView):
    model = Term
    serializer_class = TermSerializer

class TermDetail(generics.RetrieveUpdateDestroyAPIView):
    model = Term
    serializer_class = TermSerializer

