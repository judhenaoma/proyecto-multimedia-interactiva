//Arreglo de circulos y letras

ArrayList circles = new ArrayList();
ArrayList letters = new ArrayList();

//Largo y Ancho de los caracteres
int charWidth = 9;
int charHeight = 16;

//genera un color aleatorio en el que se basará la gama de colores
int tono = (int)random(360);

//Listado de Caracteres
String[] chars = {":", "&", "*", "/", "$", "?", "#", "/", "(", ")"};

void setup() {  
   size(800,800,P3D); //Tamño de la ventana
  //fullScreen(P3D, SPAN);  //Coloca la pantalla completa (corre más lento)
  
  //Cambia el modo de color de RGB a HSB, para facilitar el manejo de colores más tarde
  colorMode(HSB, 360, 100, 100);
}
//Rotar
float rotar=1;


void draw() {
  background(0); // Color negro de fondo
  for (int i=0; i<circles.size(); i++) {   //Recorre el arreglo de circulos
    ExpandingCircle ec = (ExpandingCircle) circles.get(i); //Castea el circulo como un  ExpandingCircle
    ec.update();  //Lo vuelve mas grande y lo hace menos transparente
    ec.display(); //Lo dibuja
    if (ec.transparency <= 0) { circles.remove(i); } // Si ya es transparente, lo elimina de circles
  }
  
  
  for(int j=0; j<letters.size(); j++){ //Recorre el arreglo de letras
    letter l = (letter)letters.get(j); //Obtiene la letra del arreglo
    //text(chars[(int)random(8)],mouseX,mouseY);
    l.update(); //Aumenta la trasparencia de la letra
    l.display(); //Dibuja la letra
    if (l.transparency <= 0) { letters.remove(j); } //Si ya es transparente, la elimina de letters
  }
  rotar=rotar*-1;//Se intercambia el plano en cada draw() 
  camera(mouseX, height/2, (height/2) / tan(rotar*PI/6), width/2, height/2, 0, 0, 1, 0);
  translate(width/2, height/2, 0);
  stroke(255);
  noFill();
  

  
}

class ExpandingCircle {
  int x,y;
  float radius;
  color c;
  int transparency;
  int index;
  String character;
  float computedValue;
 
  ExpandingCircle(int x, int y) {
    this.x = x;
    this.y = y;
    
    //Genera un color cercano al tono que randomizamos al principio
    //Proporciona gamas de colores estéticas
    c = color((int)(tono+random(-35,35)),random(20,100),random(40,100)); 
    
    //Escoge un caracter aleatorio
    index = int(random(chars.length));
    character = chars[index];
   
    transparency = 255; //Se genera un circulo totalmente opaco
  }
 
  void update() {
    radius++; //Aumenta el radio 
    
    //Si el radio es mayor a un numero aleatorio no mayor a 100, se aumenta la transparencia
    //La aleatoreidad le resta predecibilidad a la figura
    if (radius >= random(100) && transparency > 0) { transparency--; } 
  }
 
  void display() {                                //Muestra el circulo 
    
    //Rellena el circulo con los carecteres ASCII
    //Dibuja el circulo calculando su radio y la distancia entre sus bordes
    for (float j=x-radius; j<x+radius; j+= charHeight){ 
      for (float k = y-radius; k<y+radius; k+=charWidth){
        
        //Calcula la distancia entre (j,k) y (x,y) que es el punto de toque del mouse, o el centro del círculo
        computedValue = dist(j, k, x, y); 
        
        if (computedValue<=radius){ //si dicha distancia está dentro del radio, (j, k) pertenece al círculo
          fill(c,transparency); // Recibe el color y su transparencia
          text(character, j, k); //Escribe el caracter en (j,k)
        }
      }
    }
  }
}


class letter{
  String form;
  int posx;
  int posy;
  color c;
  int transparency;
  
  letter(int posx, int posy){
    this.form=chars[(int)random(8)];
    this.posx=posx;
    this.posy=posy;
    this.c=color(random(255),random(255),random(255));
    transparency = 255;
  }
  
  void update() {     
    if (letters.size()>500 && transparency > 0) { transparency--; } //Aumenta la transparencia
  }
  
  void display(){
    //textSize(15);
    fill(c,transparency);
    text(form, posx , posy);
  }
  
}

void mousePressed() {
  //Con cada click del mouse añade un cículo cuyo centro son las coordenadas del puntero
  circles.add(new ExpandingCircle(mouseX,mouseY));
}


void mouseDragged() 
{
  //Cada que se arrastra, añade una letra con las coordenadas del puntero
  letters.add(new letter(mouseX,mouseY));
}

void keyPressed() {
  //Borra el tablero al presionar espacio
  if (key == ' ') { circles.clear(); letters.clear(); }
  //Genera un nuevo color base al presionar cualquier tecla
  tono = (int)random(360);
}