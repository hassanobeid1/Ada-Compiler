parser grammar DecaParser;

options {
	// Default language but name it anyway
	language = Java;

	// Use a superclass to implement all helper methods, instance variables and overrides of ANTLR
	// default methods, such as error handling.

	superClass = AbstractDecaParser;

	// Use the vocabulary generated by the accompanying lexer. Maven knows how to work out the
	// relationship between the lexer and parser and will build the lexer before the parser. It will
	// also rebuild the parser if the lexer changes.
	tokenVocab = DecaLexer;
}

// which packages should be imported?
@header {
    import fr.ensimag.deca.tree.*;
    import fr.ensimag.deca.DecacCompiler;
    import java.io.PrintStream;
}

@members {
    @Override
    protected AbstractProgram parseProgram() {
        return prog().tree;
    }
}

prog
	returns[AbstractProgram tree]:
	list_classes main EOF {
            //Totalité du programme Deca
            assert($list_classes.tree != null);
            assert($main.tree != null);
            $tree = new Program($list_classes.tree, $main.tree);
            setLocation($tree, $main.start);
        };

main
	returns[AbstractMain tree]:
	/* epsilon */ {
            //Main Vide
            $tree = new EmptyMain();
        }
	| block {
            //Main
            assert($block.tree != null);
            $tree = new Main($block.tree);
            setLocation($tree, $block.start);
        };

block
	returns[BlockBody tree]:
	OBRACE list_decl list_inst CBRACE {
            //Block
            assert($list_decl.tree != null);
            assert($list_inst.tree != null);

            $tree = new BlockBody($list_decl.tree, $list_inst.tree);
            setLocation($tree, $OBRACE);
        };

list_decl
	returns[ListDeclVar tree]
	@init {
            //Liste de déclarations
            $tree = new ListDeclVar();
        }: decl_var_set[$tree]*;

decl_var_set[ListDeclVar l]:
	type list_decl_var[$l,$type.tree] SEMI;

list_decl_var[ListDeclVar l, AbstractIdentifier t]:
	dv1 = decl_var[$t] {
        //Déclaration de liste de variables
        $l.add($dv1.tree);
        } (
		COMMA dv2 = decl_var[$t] {
        $l.add($dv2.tree);
        }
	)*;

decl_var[AbstractIdentifier t]
	returns[AbstractDeclVar tree]
	@init {
        AbstractInitialization  init = new NoInitialization();
        }:
	i = ident {
        } (
		EQUALS e = expr {
        //Déclaration de variables
        init = new Initialization($e.tree);
        }
	)? {
	    setLocation(init, $i.start);
        //Fin de déclaration des variables
        $tree = new DeclVar($t, $i.tree, init);
        setLocation($tree, $i.start);
        };

list_inst
	returns[ListInst tree]
	@init {
    //Liste d'instructions
    $tree = new ListInst();
    setLocation($tree, getCurrentToken());
}: (
		i = inst {
        //Ajout d'une instruction dans la liste
        $tree.add($i.tree);
        }
	)*;

inst
	returns[AbstractInst tree]:
	e1 = expr SEMI {
            //Instruction : si expression;
            assert($e1.tree != null);
            $tree= $e1.tree;
        }
	| SEMI {
            //Instruction : si ;
            $tree = new NoOperation();
            setLocation($tree, $SEMI);
        }
	| PRINT OPARENT list_expr CPARENT SEMI {
            //Instruction : si print(list_expr);
            assert($list_expr.tree != null);
            $tree = new Print(false, $list_expr.tree);
            setLocation($tree, $list_expr.start);
        }
	| PRINTLN OPARENT list_expr CPARENT SEMI {
            //Instruction : si println(list_expr);
            assert($list_expr.tree != null);
            $tree = new Println(false, $list_expr.tree);
            setLocation($tree, $list_expr.start);
        }
	| PRINTX OPARENT list_expr CPARENT SEMI {
            //Instruction : si printx(list_expr);
            assert($list_expr.tree != null);
            $tree = new Print(true, $list_expr.tree);
            setLocation($tree, $list_expr.start);
        }
	| PRINTLNX OPARENT list_expr CPARENT SEMI {
            //Instruction : si printlnx(list_expr);
            assert($list_expr.tree != null);
            $tree = new Println(true, $list_expr.tree);
            setLocation($tree, $list_expr.start);
        }
	| if_then_else {
            //Instruction : si if_then_else
            assert($if_then_else.tree != null);
            $tree = $if_then_else.tree;
        }
	| WHILE OPARENT condition = expr CPARENT OBRACE body = list_inst CBRACE {
            //Instruction : si WHILE
            assert($condition.tree != null);
            assert($body.tree != null);
            $tree = new While($condition.tree, $body.tree);
            setLocation($tree, $condition.start);
        }
	| RETURN expr SEMI {
            //Instruction : si RETURN
            assert($expr.tree != null);
            $tree = new Return($expr.tree);
            setLocation($tree, $expr.start);
        };

if_then_else
	returns[IfThenElse tree]
	@init {
    ListInst else_inst = new ListInst();
}:
	if1 = IF OPARENT condition = expr CPARENT OBRACE li_if = list_inst CBRACE {
        //If (if_then_else)
        $tree = new IfThenElse($condition.tree, $li_if.tree, else_inst);
        setLocation($tree, $condition.start);
        } (
		ELSE elsif = IF OPARENT elsif_cond = expr CPARENT OBRACE elsif_li = list_inst CBRACE {
            //Elif (if_then_else)
            else_inst.add(new IfThenElse($elsif_cond.tree, $elsif_li.tree, new ListInst()));
        }
	)* (
		ELSE OBRACE li_else = list_inst CBRACE {
            //else (if_then_else)
            for(AbstractInst inst : $li_else.tree.getList()) {
                else_inst.add(inst);
            }
        }
	)?;

list_expr
	returns[ListExpr tree]
	@init {
    $tree = new ListExpr();
    setLocation($tree, getCurrentToken());
        }: (
		e1 = expr {
        //Liste_expr : première
        $tree.add($e1.tree);
        setLocation($tree, $e1.start);
        } (
			COMMA e2 = expr {
        //List_expr : suite
        $tree.add($e2.tree);
        }
		)*
	)?;

expr
	returns[AbstractExpr tree]:
	assign_expr {
            //expr == assign epxr
            assert($assign_expr.tree != null);
            $tree=$assign_expr.tree;
        };

assign_expr
	returns[AbstractExpr tree]:
	e = or_expr (
		/* condition: expression e must be a "LVALUE" */ {
            if (! ($e.tree instanceof AbstractLValue)) {
                throw new InvalidLValue(this, $ctx);
            }
        } EQUALS e2 = assign_expr /*Cast to Lvalue possible because we checked before*/ {
            //Assign_expr : après le =
            assert($e.tree != null);
            assert($e2.tree != null);
            $tree = new Assign((AbstractLValue) $e.tree,$e2.tree);
            setLocation($tree, $e.start);
        }
		| /* epsilon */ {
            assert($e.tree != null);
            $tree = $e.tree;
        }
	);

or_expr
	returns[AbstractExpr tree]:
	e = and_expr {
            //Or_expr : avant le or
            assert($e.tree != null);
            $tree = $e.tree;
        }
	| e1 = or_expr OR e2 = and_expr {
            //Or_expr : après le or
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Or($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
       };

and_expr
	returns[AbstractExpr tree]:
	e = eq_neq_expr {
            //And_expr : avant le and
            assert($e.tree != null);
            $tree = $e.tree;
        }
	| e1 = and_expr AND e2 = eq_neq_expr {
            //And_expr : après le and
            assert($e1.tree != null);                         
            assert($e2.tree != null);
            $tree = new And($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        };

eq_neq_expr
	returns[AbstractExpr tree]:
	e = inequality_expr {
            //eq_neq : si inégalité
            assert($e.tree != null);
            $tree = $e.tree;
        }
	| e1 = eq_neq_expr EQEQ e2 = inequality_expr {
            //eq_neq : si eq
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Equals($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
	| e1 = eq_neq_expr NEQ e2 = inequality_expr {
            //eq_neq : si neq
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new NotEquals($e1.tree, $e2.tree);
            setLocation($tree, $e2.start);
        };

inequality_expr
	returns[AbstractExpr tree]:
	e = sum_expr {
            //Inégalité : si somme
            assert($e.tree != null);
            $tree = $e.tree;
        }
	| e1 = inequality_expr LEQ e2 = sum_expr {
            //Inégalité : si leq
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new LowerOrEqual($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
	| e1 = inequality_expr GEQ e2 = sum_expr {
            //Inégalité : si geq
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new GreaterOrEqual($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
	| e1 = inequality_expr GT e2 = sum_expr {
            //inégalité : si gt
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Greater($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
	| e1 = inequality_expr LT e2 = sum_expr {
            //Inégalité : si LT
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Lower($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
	| e1 = inequality_expr INSTANCEOF type {
            //Inégalité : si InstanceOf
            assert($e1.tree != null);
            assert($type.tree != null);
            $tree = new InstanceOf($type.tree, $e1.tree);
            setLocation($tree, $INSTANCEOF);
        };

sum_expr
	returns[AbstractExpr tree]:
	e = mult_expr {
            //Somme : si mult
            assert($e.tree != null);
            $tree = $e.tree;
        }
	| e1 = sum_expr PLUS e2 = mult_expr {
            //Somme : si PLUS
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Plus($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
	| e1 = sum_expr MINUS e2 = mult_expr {
            //Somme : si MOINS
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Minus($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        };

mult_expr
	returns[AbstractExpr tree]:
	e = unary_expr {
            //Mult : si unaire
            assert($e.tree != null);
            $tree = $e.tree;
        }
	| e1 = mult_expr TIMES e2 = unary_expr {
            //mult : si MULT
            assert($e1.tree != null);                                         
            assert($e2.tree != null);
            $tree = new Multiply($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
	| e1 = mult_expr SLASH e2 = unary_expr {
            //mult : si DIVISE
            assert($e1.tree != null);                                         
            assert($e2.tree != null);
            $tree = new Divide($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
	| e1 = mult_expr PERCENT e2 = unary_expr {
            //mult : si MODULO
            assert($e1.tree != null);                                                                          
            assert($e2.tree != null);
            $tree = new Modulo($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        };

unary_expr
	returns[AbstractExpr tree]:
	op = MINUS e = unary_expr {
            //Unary : si MINUS
            assert($e.tree != null);
            $tree = new UnaryMinus($e.tree);
            setLocation($tree, $e.start);
        }
	| op = EXCLAM e = unary_expr {
            //Unary : si FACTORIELLE
            assert($e.tree != null);
            $tree = new Not($e.tree);
            setLocation($tree, $e.start);
        }
	| select_expr {
            //Unary : si select
            assert($select_expr.tree != null);
            $tree = $select_expr.tree;
        };

select_expr
	returns[AbstractExpr tree]:
	e = primary_expr {
            //Select : si primary
            assert($e.tree != null);
            $tree = $e.tree;
        }
	| e1 = select_expr DOT i = ident {
            //Select : si DOT
            assert($e1.tree != null);
            assert($i.tree != null);
            AbstractLValue value = (AbstractLValue) $e1.tree;
        } (
		o = OPARENT args = list_expr CPARENT {
            // we matched "e1.i(args)"
            assert($args.tree != null);
            $tree = new MethodCall(value, $i.tree, $args.tree);
            setLocation($tree, $e1.start);
        }
		| /* epsilon */ {
            // we matched "e.i"
            $tree = new Attribute(value, $i.tree);
            setLocation($tree, $e1.start);
        }
	);

primary_expr
	returns[AbstractExpr tree]:
	ident {
            //Primary : si IDENT
            assert($ident.tree != null);
            $tree = $ident.tree;
        }
	| m = ident OPARENT args = list_expr CPARENT {
            //Primary : si fonction
            assert($args.tree != null);
            assert($m.tree != null);
            $tree = new FunctionCall($m.tree, $args.tree);
            setLocation($tree, $m.start);
        }
	| OPARENT expr CPARENT {
            //Primary : si expr dans PARANTHESES
            assert($expr.tree != null);
            $tree = $expr.tree;
        }
	| READINT OPARENT CPARENT {
            //Primary : si READINT
            $tree = new ReadInt();
            setLocation($tree, $READINT);
        }
	| READFLOAT OPARENT CPARENT {
            //Primary : si READFLOAT
            $tree = new ReadFloat();
            setLocation($tree, $READFLOAT);
        }
	| NEW ident OPARENT CPARENT {
            //Primary : si NEW
            assert($ident.tree != null);
            $tree = new New($ident.tree);
            setLocation($tree, $NEW);
        }
	| cast = OPARENT type CPARENT OPARENT expr CPARENT {
            //Primary : si CAST
            assert($type.tree != null);
            assert($expr.tree != null);
            $tree = new Cast($type.tree, $expr.tree);
            setLocation($tree, $type.start);
        }
	| literal {
            //Primary : si CAST
            assert($literal.tree != null);
            $tree = $literal.tree;
        };

type
	returns[AbstractIdentifier tree]:
	ident {
            //Type
            assert($ident.tree != null);
            $tree = $ident.tree;
        };

literal
	returns[AbstractExpr tree]:
	INT {
            //Litteral : si INT
            //Litteral : si INT
            $tree = new IntLiteral(Integer.parseInt($INT.text));
            setLocation($tree, $INT);
        }
	| fd = FLOAT {
            //Litteral : si FLOAT
            $tree = new FloatLiteral(Float.parseFloat($fd.text));
            setLocation($tree, $fd);
        }
	| STRING {
            //Litteral : si STRING
            $tree = new StringLiteral($STRING.text);
            setLocation($tree, $STRING);
        }
	| TRUE {
            //Litteral : si TRUE
            $tree = new BooleanLiteral(true);
            setLocation($tree, $TRUE);
        }
	| FALSE {
            //Litteral : si FALSE
            $tree = new BooleanLiteral(false);
            setLocation($tree, $FALSE);
        }
	| THIS {
            //Litteral : si THIS
            $tree = new This();
            setLocation($tree, $THIS);
        }
	| NULL {
            //Litteral : si NULL
            $tree = new Null();
            setLocation($tree, $NULL);
        };

ident
	returns[AbstractIdentifier tree]:
	IDENT {
            //IDENT
            $tree = new Identifier(getDecacCompiler().getSymbol($IDENT.text));
            setLocation($tree, $IDENT);
        };

/****     Class related rules     ****/

list_classes
	returns[ListDeclClass tree]
	@init {
        $tree = new ListDeclClass();
        setLocation($tree, getCurrentToken());
    }:
	(
		c1 = class_decl {
		    assert($c1.tree != null);
            $tree.add($c1.tree);
        }
	)*;

class_decl
	returns[DeclClass tree]
	@init {
        ListDeclField fields = new ListDeclField();
        ListDeclMethod methods = new ListDeclMethod();
    }:
	CLASS name = ident superclass = class_extension OBRACE class_body[fields, methods] CBRACE {
	            assert($name.tree != null);
    	        assert($superclass.tree != null);
                $tree = new DeclClass($name.tree, $superclass.tree, fields, methods);
                setLocation($tree, $CLASS);
	};

class_extension
	returns[AbstractIdentifier tree]:
	EXTENDS ident {
	        assert($ident.tree != null);
	        $tree = $ident.tree;
        }
	| /* epsilon */ {
	        $tree = new Identifier(getDecacCompiler().getSymbol("Object"));
	        setLocation($tree, getCurrentToken());
        };

class_body[ListDeclField fields, ListDeclMethod methods]: (
		m = decl_method[$methods] {
        }
		| decl_field_set[$fields]
	)*;

decl_field_set[ListDeclField fields]:
	v = visibility t = type list_decl_field[$fields, $v.tree, $t.tree] SEMI;

visibility
	returns[Visibility tree]:
	/* epsilon */ {
	    $tree = Visibility.PUBLIC;
	    }
	| PROTECTED {
	        $tree = Visibility.PROTECTED;
        };

list_decl_field[ListDeclField fields, Visibility v, AbstractIdentifier t]:
	dv1 = decl_field[$v, $t] {
    assert($dv1.tree != null);
    $fields.add($dv1.tree);
} (
		COMMA dv2 = decl_field[$v, $t]{
    assert($dv2.tree != null);
    $fields.add($dv2.tree);
}
	)*;

decl_field[Visibility v, AbstractIdentifier t]
	returns[DeclField tree]:
	i = ident {
	        assert($i.tree != null);
	        AbstractIdentifier name = $i.tree;
	        AbstractInitialization value = new NoInitialization();
	        setLocation(value, $i.start);
        } (
		EQUALS e = expr {
		    assert($e.tree != null);
		    value = new Initialization($e.tree);
		    setLocation(value, $e.start);
        }
	)? {
	        assert($v != null);
	        assert($t != null);
	        $tree = new DeclField($v, $t, name, value);
	        setLocation($tree, $i.start);
        };

decl_method[ListDeclMethod methods]
	@init {
	   AbstractBody body;
}:
	type ident OPARENT params = list_params CPARENT (
		block {
		    assert($block.tree != null);
		    body = $block.tree;
        }
		| ASM OPARENT code = multi_line_string CPARENT SEMI {
		    assert($code.text != null);
		    body = new AsmBody($code.text);
		    setLocation(body, $ASM);

        }
	) {
	    assert($ident.tree != null);
	    assert($type.tree != null);
	    assert($params.tree != null);
	    AbstractDeclMethod method = new DeclMethod($ident.tree, $type.tree, $params.tree, body);
	    setLocation(method, $type.start);
        $methods.add(method);
        };

list_params
	returns[ListParameter tree]
	@init {
		    $tree = new ListParameter();
            setLocation($tree,getCurrentToken());
    }: (
		p1 = param {
		    assert($p1.tree != null);
		    $tree.add($p1.tree);

        } (
			COMMA p2 = param {
			assert($p2.tree != null);
			$tree.add($p2.tree);
        }
		)*
	)?;

multi_line_string
	returns[String text, Location location]:
	s = STRING {
            $text = $s.text;
            $location = tokenLocation($s);
        }
	| s = MULTI_LINE_STRING {
            $text = $s.text;
            $location = tokenLocation($s);
        };

param
	returns[Parameter tree]:
	type ident {
	    $tree = new Parameter($type.tree, $ident.tree);
	    setLocation($tree, $type.start);
        };