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
 * Copyright (C) 2015-2018 CNRS (Lab-STICC UMR CNRS 6285)
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
package org.orbisgis.orbiswps.scripts.scripts.Import

import org.orbisgis.orbiswps.groovyapi.input.*
import org.orbisgis.orbiswps.groovyapi.output.*
import org.orbisgis.orbiswps.groovyapi.process.*

/**
 * @author Erwan Bocher
 */
@Process(title = "Import a CSV file",
    description = "Import in the database a CSV file as a new table.",
    keywords = "OrbisGIS,Import, File, CSV",
    properties = ["DBMS_TYPE","H2GIS"],
    version = "1.0")
def processing() {
    File csvFile = new File(csvDataInput[0])
    name = csvFile.getName()
    tableName = name.substring(0, name.lastIndexOf(".")).toUpperCase()
    if(jdbcTableOutputName != null){
	    tableName = jdbcTableOutputName
    }
    if(dropTable){
	    sql.execute "drop table if exists " + tableName
    }

    String csvRead = "CSVRead('"+csvFile.absolutePath+"', NULL, 'fieldSeparator="+separator+"')";
    String create = "CREATE TABLE "+ tableName ;    
    sql.execute(create + " AS SELECT * FROM "+csvRead+";");   

    literalDataOutput = i18n.tr("The CSV file has been imported.")
}


@RawDataInput(
    title = "Input CSV",
    description = "The input CSV file to be imported.",
    fileTypes = ["csv"],
    isDirectory = false)
String[] csvDataInput


@EnumerationInput(
    title = "CSV separator",
    description = "The CSV separator.",
    values = [",", "\t", " ", ";"],
    names = "Coma, Tabulation, Space, Semicolon",
    isEditable = true)
String[] separator = [";"]




@LiteralDataInput(
    title = "Drop the existing table",
    description = "Drop the existing table.")
Boolean dropTable 



/** Optional table name. */
@LiteralDataInput(
    title = "Output table name",
    description = "Table name to store the CSV file. If it is not defined the name of the file will be used.",
    minOccurs = 0)
String jdbcTableOutputName




/************/
/** OUTPUT **/
/************/
@LiteralDataOutput(
    title = "Output message",
    description = "Output message.")
String literalDataOutput
