import eu.mihosoft.vrl.v3d.CSG
import eu.mihosoft.vrl.v3d.Cube
import eu.mihosoft.vrl.v3d.Cylinder
import eu.mihosoft.vrl.v3d.Transform

def name
def piece_width
def piece_height
def border_width
def border_thickness
def do_rabbet
if(args==null){
	name = "pandemonium"
	piece_width = 236
	piece_height = 155.8
	border_width = 7
	border_thickness = 4
	do_rabbet = false
	CSG borders_rabbet_false, borders_rabbet_true
	ArrayList<Object> borders_rabbet_false_params = new ArrayList<Object>()
	borders_rabbet_false_params.add(name)
	borders_rabbet_false_params.add(piece_width)
	borders_rabbet_false_params.add(piece_height)
	borders_rabbet_false_params.add(border_width)
	borders_rabbet_false_params.add(border_thickness)
	borders_rabbet_false_params.add(do_rabbet)
	borders_rabbet_false =  (CSG)ScriptingEngine.gitScriptRun(
		"https://github.com/JansenSmith/ArtBorders.git", // git location of the library
		  "ArtBorders.groovy" , // file to load
		  borders_rabbet_false_params // send the factory the name param
	)
	do_rabbet = true
	ArrayList<Object> borders_rabbet_true_params = new ArrayList<Object>()
	borders_rabbet_true_params.add(name)
	borders_rabbet_true_params.add(piece_width)
	borders_rabbet_true_params.add(piece_height)
	borders_rabbet_true_params.add(border_width)
	borders_rabbet_true_params.add(border_thickness)
	borders_rabbet_true_params.add(do_rabbet)
	borders_rabbet_true =  (CSG)ScriptingEngine.gitScriptRun(
		"https://github.com/JansenSmith/ArtBorders.git", // git location of the library
		  "ArtBorders.groovy" , // file to load
		  borders_rabbet_true_params // send the factory the name param
	)
	borders_rabbet_false = borders_rabbet_false.setColor(javafx.scene.paint.Color.DARKGRAY)
		.setName(name+"_base")
		.addAssemblyStep(0, new Transform())
		.setIsWireFrame(true)
		.setManufacturing({ toMfg ->
			return toMfg
					//.rotx(180)// fix the orientation
					//.toZMin()//move it down to the flat surface
		})
		
	borders_rabbet_true = borders_rabbet_true.setColor(javafx.scene.paint.Color.DARKRED)
		.setName(name+"_base")
		.addAssemblyStep(0, new Transform())
		.setManufacturing({ toMfg ->
			return toMfg
					//.rotx(180)// fix the orientation
					//.toZMin()//move it down to the flat surface
		})
		
	return borders_rabbet_true//, borders_rabbet_false]
} else if (args.size() >= 5 && args.get(5)) {
	name = args.get(0)
	piece_width = args.get(1)
	piece_height = args.get(2)
	border_width = args.get(3)
	border_thickness = args.get(4)
	do_rabbet = args.get(5)
	println "Nesting constructor to create a rabbeted border with these parameters: name = "+name+
				", piece_width = "+piece_width+
				", piece_height = "+piece_height+
				", border_width = "+border_width+
				", border_thickness = "+border_thickness+
				", do_rabbet = "+do_rabbet
	CSG border_internal, border_external
	do_rabbet = false
	def border_internal_width = border_width / 2
	def border_internal_thickness = border_thickness
	ArrayList<Object> border_internal_params = new ArrayList<Object>()
	border_internal_params.add(name)
	border_internal_params.add(piece_width)
	border_internal_params.add(piece_height)
	border_internal_params.add(border_internal_width)
	border_internal_params.add(border_internal_thickness)
	border_internal_params.add(do_rabbet)
	border_internal =  (CSG)ScriptingEngine.gitScriptRun(
		"https://github.com/JansenSmith/ArtBorders.git", // git location of the library
		  "ArtBorders.groovy" , // file to load
		  border_internal_params // send the factory the name param
	)
	def border_external_width = border_width
	def border_external_thickness = border_thickness / 2
	ArrayList<Object> border_external_params = new ArrayList<Object>()
	border_external_params.add(name)
	border_external_params.add(piece_width)
	border_external_params.add(piece_height)
	border_external_params.add(border_external_width)
	border_external_params.add(border_external_thickness)
	border_external_params.add(do_rabbet)
	border_external =  (CSG)ScriptingEngine.gitScriptRun(
		"https://github.com/JansenSmith/ArtBorders.git", // git location of the library
		  "ArtBorders.groovy" , // file to load
		  border_external_params // send the factory the name param
	)
	ret = border_internal.union(border_external)
	return ret
} else {
	name = args.get(0)
	piece_width = args.get(1)
	piece_height = args.get(2)
	border_width = args.get(3)
	border_thickness = args.get(4)
	do_rabbet = args.get(5)
	println "Parameters sent to border constructor: name = "+name+
				", piece_width = "+piece_width+
				", piece_height = "+piece_height+
				", border_width = "+border_width+
				", border_thickness = "+border_thickness+
				", do_rabbet = "+do_rabbet
}

def width = piece_width // + 2*border_width
def height = piece_height // + 2*border_width

CSG bottom = new Cube(width, border_width, border_thickness).toCSG()
					.toZMin()
					.toYMax()
					.toXMin()
					//.movex(-border_width)
CSG top = bottom.toYMin().movey(piece_height)

CSG left = new Cube(border_width, height, border_thickness).toCSG()
					.toZMin()
					.toXMax()
					.toYMin()
					//.movey(-border_width)
CSG right = left.toXMin().movex(piece_width)
CSG borders = bottom.union(top).union(left).union(right)

CSG rounding = new Cylinder(border_width, border_thickness, (int)32).toCSG()
					.toZMin()

borders = borders.union(rounding)
					.union(rounding.movex(width))
					.union(rounding.movey(height))
					.union(rounding.movex(width).movey(height))
					
CSG cutout = new Cube(width, height, border_thickness).toCSG()
					.toXMin()
					.toYMin()
					.toZMin()
					
borders = borders.difference(cutout)

if(do_rabbet) {
	
}

CSG ret = borders
		
return ret
