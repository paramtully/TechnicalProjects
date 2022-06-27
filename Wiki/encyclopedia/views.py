from django.http import HttpResponseRedirect
from django.shortcuts import render
from django.urls import reverse
from django import forms
from random import choice
from markdown2 import Markdown

from . import util

SEARCH_PROMPT = "Search Qwikipedia"
EDIT_PROMPT = "Enter Page Content using Github Markdown"
NOT_FOUND_ERROR = "...page does not yet exist."
ALREADY_EXISTS_ERROR = "already exists..."

class SearchForm(forms.Form):
    title = forms.CharField(label ='', widget=forms.TextInput(attrs={
        "class": "search",
        "placeholder": SEARCH_PROMPT,
        "name": "q"
    }))

class CreateForm(forms.Form):
    title = forms.CharField(label='', widget=forms.TextInput(attrs={
        "placeholder": "Entry Title"
    }))
    entry = forms.CharField(label='', widget=forms.Textarea(attrs={
        "placeholder": "Your entry here..."
    }))

class EditForm(forms.Form):
    title = forms.CharField(label='', widget=forms.TextInput(attrs={
        "class": "edit_title",
        "readonly": "readonly"
    }))
    entry = forms.CharField(label='', widget=forms.Textarea(attrs={
        "placeholder": EDIT_PROMPT
    }))


def index(request):
    """ Displays home page """
    return render(request, "encyclopedia/index.html", {
        "entries": util.list_entries(),
        "search_form": SearchForm()
    })

def wiki(request, title):
    """ Displays the requested entry page, if it exists """
    entry = util.get_entry(title)
    if entry:
        entry_HTML = Markdown().convert(entry)
        return render(request, "encyclopedia/wiki.html", {
            "title": title,
            "entry": entry_HTML,
            "search_form": SearchForm()
        })
    else:
        return render(request, "encyclopedia/error.html", {
            "title": title,
            "error": NOT_FOUND_ERROR,
            "search_form": SearchForm()
        })

def search(request):
    if request.method == "POST":
        form = SearchForm(request.POST)
        if form.is_valid():
            title =  form.cleaned_data["title"]
            if util.get_entry(title):
                return HttpResponseRedirect(reverse("wiki", args=[title]))
            else:
                return render(request, "encyclopedia/search.html", {
                    "titles": util.list_related_entries(title),
                    "search_form": SearchForm()
                })
    return HttpResponseRedirect(reverse("index"))

def create(request):
    if request.method == "POST":
        new_entry = CreateForm(request.POST)
        if new_entry.is_valid():
            title = new_entry.cleaned_data["title"]
            entry = new_entry.cleaned_data["entry"]
            if not util.get_entry(title):
                util.save_entry(title, entry)
                return HttpResponseRedirect(reverse("wiki", args=[title]))
            else:
                return render(request, "encyclopedia/error.html", {
                    "title": title,
                    "error": ALREADY_EXISTS_ERROR,
                    "search_form": SearchForm()
                })
    return render(request, "encyclopedia/create.html", {
        "create_form": CreateForm(),
        "search_form": SearchForm()
    })

def edit(request, title):
    if request.method == "POST":
        form = EditForm(request.POST)
        if form.is_valid():
            entry = form.cleaned_data["entry"]
            util.save_entry(title, entry)
            return HttpResponseRedirect(reverse("wiki", args=[title]))
    return render(request, "encyclopedia/edit.html", {
        "title": title,
        "search_form": SearchForm(),
        "edit_form": EditForm(initial={
            "title": title,
            "entry": util.get_entry(title).rsplit("\n\n", 1)[1]
        })
    })

def random(request):
    titles = util.list_entries()
    title = choice(titles)
    return HttpResponseRedirect(reverse("wiki", args=[title]))