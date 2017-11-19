/*
 * OrbisWPS contains a set of libraries to build a Web Processing Service (WPS)
 * compliant with the 2.0 specification.
 *
 * OrbisWPS is part of the OrbisGIS platform
 *
 * OrbisGIS is a java GIS application dedicated to research in GIScience.
 * OrbisGIS is developed by the GIS group of the DECIDE team of the
 * Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
 *
 * The GIS group of the DECIDE team is located at :
 *
 * Laboratoire Lab-STICC – CNRS UMR 6285
 * Equipe DECIDE
 * UNIVERSITÉ DE BRETAGNE-SUD
 * Institut Universitaire de Technologie de Vannes
 * 8, Rue Montaigne - BP 561 56017 Vannes Cedex
 *
 * OrbisWPS is distributed under GPL 3 license.
 *
 * Copyright (C) 2015-2017 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * OrbisWPS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * OrbisWPS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * OrbisWPS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.orbiswps.scripts.scripts.Select

import org.orbiswps.groovyapi.input.*
import org.orbiswps.groovyapi.output.*
import org.orbiswps.groovyapi.process.*
import org.h2gis.utilities.SFSUtilities
import org.h2gis.utilities.TableLocation


/**
 * @author Erwan Bocher
 */
@Process(
    title = [
				"Attribut selection","en",
				"Selection par attribut","fr"],
    description = [
				"Select rows from one table based on one column value. SQL expression could be used to custom the select. ","en",
				"Selectionnez des lignes dans une table à partir de la valeur d'une colonne. La syntaxe SQL peut être combinée pour personnaliser la sélection.","fr"],
    keywords = ["Filtering", "en",
				"Sélection", "fr"],
    properties = ["DBMS_TYPE", "H2GIS",
				"DBMS_TYPE", "POSTGIS"],
    version = "1.0",
    identifier = "orbisgis:wps:official:selectAttribute"
)
def processing() {
    
    //Build the start of the query
    String  outputTable = fromSelectedTable+"_filtered"

    if(outputTableName != null){
	outputTable  = outputTableName
    }

    String query = "CREATE TABLE " + outputTable + " AS SELECT a.*"
    query += " from " +  fromSelectedTable+ " as a  where " + fromSelectedColumn[0] + " "+ operation[0] + " "+ fromSelectedValue
 
    if(dropTable){
	sql.execute "drop table if exists " + outputTable
    }
    //Execute the query
    sql.execute(query)

    literalOutput = "Process done"
    
}

/****************/
/** INPUT Data **/
/****************/

@JDBCTableInput(
    title = [
				"Table to select from","en",
				"Entités à selectionner","fr"],
    description = [
				"The spatial data source that contains the selected features.","en",
				"La table qui contient les entités à selectionner.","fr"]
)
String fromSelectedTable


@JDBCColumnInput(
    title = [
				"Geometric column from","en",
				"Colonne géométrique","fr"],
    description = [
				"The geometric column of selected table.","en",
				"La colonne géométrique de la table avec les entités à selectionner","fr"],
    jdbcTableReference = "fromSelectedTable",
    excludedTypes = ["GEOMETRY"]
)
String[] fromSelectedColumn


@EnumerationInput(
    title = ["Operator","en",
				"Opérateur","fr"],
    description = [
				"Operator to select rows.","en",
				"Opérateur pour séléctionner les lignes.","fr"],
    values=["=", ">",">=", "<", "<=","<>", "limit", "in", "not in", "like"],
    names=["Equal to, Greater than, Greater than or equal to, Less than, Less than or equal to, Not equal to,Limit, In, Not In, Like", "en", "Egal à, Supérieur à, Supérieur ou égal à, Inférieur à, Inférieur ou égal à,Non égal à,Limite, Contient, Ne contient pas, Comme ",  "fr"])
String[] operation = ["="]

@LiteralDataInput(
    title = [
				"Value","en",
				"Valeur","fr"],
    description = [
				"Value to select or any syntax supported by SQL as function on value.","en",
				"Valeur à selectionner ou syntaxe supportée par le SQL comme l'utilisation d'une fonction ou une référence vers une autre colonne.","fr"])
String fromSelectedValue  



@LiteralDataInput(
    title = [
				"Drop the output table if exists","en",
				"Supprimer la table de sortie si elle existe","fr"],
    description = [
				"Drop the output table if exists.","en",
				"Supprimer la table de sortie si elle existe.","fr"],
    minOccurs = 0)
Boolean dropTable 

@LiteralDataInput(
    title = [
				"Output table name","en",
				"Nom de la table de sortie","fr"],
    description = [
				"Name of the table containing the result of the process.","en",
				"Nom de la table contenant les résultats du traitement.","fr"],
    minOccurs = 0,
    identifier = "outputTableName"
)
String outputTableName 



/** String output of the process. */
@LiteralDataOutput(
    title = [
				"Output message","en",
				"Message de sortie","fr"],
    description = [
				"The output message.","en",
				"Le message de sortie.","fr"],
    identifier = "literalOutput"
)
String literalOutput


