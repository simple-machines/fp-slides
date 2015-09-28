MD=markdown-to-slides

all: slides

clean:
	rm *.html

%.html: %.md
	$(MD) $< -o $@


slides: part1.html part2.html

