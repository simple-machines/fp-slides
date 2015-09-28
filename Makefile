MD=markdown-to-slides

all: slides

clean:
	rm *.html

part1:
	$(MD) part1.md -o part1.html

part2:
	$(MD) part2.md -o part2.html

slides: part1 part2

