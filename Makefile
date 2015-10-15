MD=markdown-to-slides

all: slides

clean:
	rm *.html

%.html: %.md
	$(MD) $< -o $@


slides: typeclasses.html monoids.html type-tricks.html
