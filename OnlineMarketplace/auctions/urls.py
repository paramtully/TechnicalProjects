from django.urls import path

from django.conf import settings            # required to display images
from django.conf.urls.static import static

from . import views

urlpatterns = [
    path("", views.index, name="index"),
    path("login", views.login_view, name="login"),
    path("logout", views.logout_view, name="logout"),
    path("register", views.register, name="register"),
    path("create", views.create, name="create"), 
    path("listing/<str:item_id>", views.listing, name="listing"),
    path("watch", views.watch, name="watch"),
    path("myListings", views.myListings, name="myListings"),
    path("myPurchases", views.myPurchases, name="myPurchases")
]

urlpatterns += static(settings.MEDIA_URL, document_root = settings.MEDIA_ROOT)  # to access images
