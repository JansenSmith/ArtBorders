import eu.mihosoft.vrl.v3d.CSG
import eu.mihosoft.vrl.v3d.Cube

def name
def piece_width
def piece_height
def border_width
def border_thickness
if(args==null){
	name = "pandemonium"
	piece_width = 236
	piece_height = 155.8
	border_width = 7
	border_thickness = 4
	println "No parameters found. Using name = "+name+
				", piece_width = "+piece_width+
				", piece_height = "+piece_height+
				", border_width = "+border_width+
				", border_thickness = "+border_thickness
} else {
	name = args.get(0)
	println "Parameters sent to border constructor: name = "+name+
				", piece_width = "+piece_width+
				", piece_height = "+piece_height+
				", border_width = "+border_width+
				", border_thickness = "+border_thickness
}

def width = piece_width + 2*border_width
def height = piece_height + 2*border_width

CSG bottom = new Cube(width, border_width, border_thickness).toCSG()
					.toZMin()
					.toYMax()
					.toXMin()
					.movex(-border_width)
CSG top = bottom.toYMin().movey(piece_height)

CSG left = new Cube(border_width, height, border_thickness).toCSG()
					.toZMin()
					.toXMax()
					.toYMin()
					.movey(-border_width)
CSG right = left.toXMin().movex(piece_width)

CSG ret = bottom.union(top).union(left).union(right)
		
return ret
