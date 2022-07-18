from sre_parse import CATEGORIES
from django.contrib.auth import authenticate, login, logout
from django.db import IntegrityError
from django.http import HttpResponseRedirect
from django.shortcuts import render
from django.urls import reverse
from django.contrib.auth.decorators import login_required

from .models import AuctionListing, User, CATEGORIES, WatchList
from .forms import BidForm, CreateForm, CommentForm
from decimal import Decimal

INVALID_FORM_ERROR = "Invalid entry..."
BID_TOO_LOW_ERROR = "Bid too low..."
BID_SUCESS = "Bid placed successfully."

def login_view(request):
    if request.method == "POST":

        # Attempt to sign user in
        username = request.POST["username"]
        password = request.POST["password"]
        user = authenticate(request, username=username, password=password)

        # Check if authentication successful
        if user is not None:
            login(request, user)
            return HttpResponseRedirect(reverse("index"))
        else:
            return render(request, "auctions/login.html", {
                "message": "Invalid username and/or password."
            })
    else:
        return render(request, "auctions/login.html")


def logout_view(request):
    logout(request)
    return HttpResponseRedirect(reverse("index"))


def register(request):
    if request.method == "POST":
        username = request.POST["username"]
        email = request.POST["email"]

        # Ensure password matches confirmation
        password = request.POST["password"]
        confirmation = request.POST["confirmation"]
        if password != confirmation:
            return render(request, "auctions/register.html", {
                "message": "Passwords must match."
            })

        # Attempt to create new user
        try:
            user = User.objects.create_user(username, email, password)
            user.save()
        except IntegrityError:
            return render(request, "auctions/register.html", {
                "message": "Username already taken."
            })
        login(request, user)
        return HttpResponseRedirect(reverse("index"))
    else:
        return render(request, "auctions/register.html")


def index(request):
    return render(request, "auctions/index.html", {
        "listings": AuctionListing.objects.all(),
        "watchCount": len(request.user.watching.all()) if request.user.is_authenticated else 0
    })


def getMaxBid(listing):
    maxBid = None
    for bid in listing.bids.all():
        if not maxBid or bid.amount > maxBid.amount: maxBid = bid
    return maxBid


@login_required(login_url="login")
def create(request):
    if request.method == "POST":
        # saves auction listing to database if valid
        form = CreateForm(request.POST, request.FILES)  # request.FILES hold files such as png
        if form.is_valid():
            listing = form.save(commit=False)
            listing.seller = request.user
            listing.save()
            return HttpResponseRedirect(reverse("listing", args=(listing.id,)))
        # otherwise return page with error message 
        else:
            return render(request, "auctions/create.html", {
                "createForm": form,
                "watchCount": len(request.user.watching.all()),
                "message": INVALID_FORM_ERROR
            })

    return render(request, "auctions/create.html", {
        "watchCount": len(request.user.watching.all()),
        "createForm": CreateForm()
    })


@login_required(login_url="login")
def listing(request, item_id):

    # check if post request and process
    message = None
    if request.method == "POST":
        if "watch" in request.POST or "unwatch" in request.POST:
            processWatch(request, item_id)
        elif "bidAmount" in request.POST:
            response = processBid(request, item_id)
            message = response
        elif "close" in request.POST:
            processCloseListing(item_id)
        elif "comment" in request.POST:
            processComment(request, item_id)

    # attempts load listing page
    try:
        listing = AuctionListing.objects.get(id=int(item_id))
        print(listing.category)
        if listing:
            maxBid = getMaxBid(listing)

            return render(request, "auctions/listing.html", {
                "listing": listing,
                "watchCount": len(request.user.watching.all()),
                "category": CATEGORIES[int(listing.category)][1], 
                "watching": any(listing == watchlist.listing for watchlist in request.user.watching.all()),
                "bidCount": len(listing.bids.all()),
                "highestBid": maxBid.amount if maxBid else 0,
                "userIsHighestBid": maxBid.bidder == request.user if maxBid else False,
                "bidForm": BidForm(),
                "commentForm": CommentForm(),
                "comments": listing.comments.all(),
                "message": message
            })
    except Exception as e:
        print(e)
        return render(request, "auctions/error.html", {
                "watchCount": len(request.user.watching.all()),
                "error": "Listing does not exist."
            })


def processBid(request, item_id):
    form = BidForm(request.POST)
    if form.is_valid():
        listing = AuctionListing.objects.get(id=int(item_id))
        lastBid = max([bid.amount for bid in listing.bids.all()], default=(listing.price - round(Decimal(0.01), ndigits=2)))

        if form.cleaned_data["amount"] <= lastBid: return BID_TOO_LOW_ERROR
        bid = form.save(commit=False)
        bid.bidder = request.user
        bid.save()

        listing.price = bid.amount + round((Decimal(0.01)), ndigits=2)
        listing.bids.add(bid)
        listing.save()

        return BID_SUCESS
    return INVALID_FORM_ERROR


def processWatch(request, item_id):
    listing = AuctionListing.objects.get(id=int(item_id))
    watch = WatchList()

    watch.watcher = request.user
    watch.listing = listing

    if "watch" in request.POST: watch.save()
    else: request.user.watching.get(listing=int(item_id)).delete()


def processCloseListing(item_id):
    listing = AuctionListing.objects.get(id=int(item_id))
    listing.buyer = getMaxBid(listing).bidder if len(listing.bids.all()) else None
    listing.closed = True
    listing.save()


def processComment(request, item_id):
    listing = AuctionListing.objects.get(id=int(item_id))
    form = CommentForm(request.POST)
    
    if form.is_valid():
        comment = form.save(commit=False)
        comment.commenter = request.user
        comment.save()
        listing.comments.add(comment)
        listing.save()


@login_required(login_url="login")
def watch(request):
    return render(request, 'auctions/watch.html', {
        "watchlists": request.user.watching.all(),
        "watchCount": len(request.user.watching.all())
    })

@login_required(login_url="login")
def myListings(request):
    return render(request, "auctions/myListings.html", {
        "myListings": request.user.selling.all(),
        "watchCount": len(request.user.watching.all())
    })

    
    
    



